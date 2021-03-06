package cn.cebest.util.customform;


/* ====================================================================
        Licensed to the Apache Software Foundation (ASF) under one or more
        contributor license agreements.  See the NOTICE file distributed with
        this work for additional information regarding copyright ownership.
        The ASF licenses this file to You under the Apache License, Version 2.0
        (the "License"); you may not use this file except in compliance with
        the License.  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
        ==================================================================== */


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Logger;
import cn.cebest.util.SpringUtil;
import cn.cebest.util.ThreadPoolUtils;
import cn.cebest.util.UuidUtil;
import cn.cebest.util.customform.FormXSSFSheetXMLHandler.SheetContentsHandler;

/**
 * A rudimentary XLSX -> CSV processor modeled on the
 * POI sample program XLS2CSVmra from the package
 * org.apache.poi.hssf.eventusermodel.examples.
 * As with the HSSF version, this tries to spot missing
 * rows and cells, and output empty entries for them.
 * <p/>
 * Data sheets are read using a SAX parser to keep the
 * memory footprint relatively small, so this should be
 * able to read enormous workbooks.  The styles table and
 * the shared-string table must be kept in memory.  The
 * standard POI styles table class is used, but a custom
 * (read-only) class is used for the shared string table
 * because the standard POI SharedStringsTable grows very
 * quickly with the number of unique strings.
 * <p/>
 * For a more advanced implementation of SAX event parsing
 * of XLSX files, see {@link XSSFEventBasedExcelExtractor}
 * and {@link XSSFSheetXMLHandler}. Note that for many cases,
 * it may be possible to simply use those with a custom
 * {@link SheetContentsHandler} and no SAX code needed of
 * your own!
 */
public class XLSX2CSV {
	protected static Logger logger = Logger.getLogger(XLSX2CSV.class);

    private final OPCPackage xlsxPackage;

    /**
     * Number of columns to read starting with leftmost
     */
//    private final int minColumns;

    private ThreadPoolUtils threadPool=new ThreadPoolUtils(
    		ThreadPoolUtils.FixedThread,ThreadPoolUtils.FixedThread_NUM);
    /**
     * Destination for data
     */
//    private final PrintStream output;
    private List<CustomFormAttribute> attrInfoList=null;

    private static final int SPLITCOUNT=100;
    
    private final ConcurrentHashMap<Integer,List<String>> rowMap = 
    		new ConcurrentHashMap<Integer,List<String>>();

    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     * of parsing the Sheet XML, and outputs the contents
     * as a (basic) CSV.
     */
    private class SheetToCSV implements SheetContentsHandler {
//        private boolean firstCellOfRow = false;
        private int currentRow = -1;
        private int currentCol = -1;

//        private void outputMissingRows(int number) {
//            for (int i = 0; i < number; i++) {
//                for (int j = 0; j < minColumns; j++) {
//                    output.append(',');
//                }
//                output.append('\n');
//            }
//        }

        @Override
        public void startRow(int rowNum) {
            // If there were gaps, output the missing rows
//            outputMissingRows(rowNum - currentRow - 1);
            // Prepare for this row
//            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
            // Ensure the minimum number of columns
//            for (int i = currentCol; i < minColumns; i++) {
//               output.append(',');
//            }
            if(currentRow!=0 
            		&& currentRow%(SPLITCOUNT*attrInfoList.size())==0){
            	if(rowMap!=null && !rowMap.isEmpty()){
            		final List<CustomformAttributeValue> dataList = 
            				new ArrayList<CustomformAttributeValue>();
            		for(Map.Entry<Integer,List<String>> row:rowMap.entrySet()){
            			List<String> rowData=row.getValue();
            			//如果此时间不能保证唯一性，则还原为上面方法
            			long createdTime=System.nanoTime();
            			int count=0;
            			for(String data:rowData){
            				CustomformAttributeValue customformAttributeValue = 
            						new CustomformAttributeValue();
            				customformAttributeValue.setCreatedTime(createdTime);
            				customformAttributeValue.setAttrId(attrInfoList.get(count++).getId());
            				customformAttributeValue.setId(UuidUtil.get32UUID());
            				customformAttributeValue.setAttrValue(data);
            				dataList.add(customformAttributeValue);
            			}
            			int size=attrInfoList.size();
            			if(count<size){
            				for(int i=count;i<size;i++){
                				CustomformAttributeValue customformAttributeValue = 
                						new CustomformAttributeValue();
                				customformAttributeValue.setCreatedTime(createdTime);
                				customformAttributeValue.setAttrId(attrInfoList.get(count++).getId());
                				customformAttributeValue.setId(UuidUtil.get32UUID());
                				customformAttributeValue.setAttrValue(StringUtils.EMPTY);
                				dataList.add(customformAttributeValue);
            				}
            			}
            		}
            		rowMap.clear();
            		threadPool.execute(new Runnable() {
						@Override
						public void run() {
							try {
								SpringUtil.getBean(CustomformAttributeValueService.class).
								saveBatch(dataList);
							} catch (Exception e) {
								logger.error("save the form data occured error!", e);
							}
						}
					});
            	}
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                         XSSFComment comment) {
            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }
            if(currentRow==0) return;
            // Did we miss any cells?
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            for (int i = 0; i < missedCols; i++) {
            	if(rowMap.containsKey(currentRow)){
            		rowMap.get(currentRow).add(StringUtils.EMPTY);
            	}else{
            		List<String> list=new ArrayList<String>();
            		list.add(StringUtils.EMPTY);
            		rowMap.put(new Integer(currentRow), list);
            	}
            }
            currentCol = thisCol;

            // Number or string?
            try {
                Double.parseDouble(formattedValue);
            	if(rowMap.containsKey(currentRow)){
            		rowMap.get(currentRow).add(String.valueOf(formattedValue));
            	}else{
            		List<String> list=new ArrayList<String>();
            		list.add(String.valueOf(formattedValue));
            		rowMap.put(new Integer(currentRow), list);
            	}

            } catch (NumberFormatException e) {
            	if(rowMap.containsKey(currentRow)){
            		rowMap.get(currentRow).add(formattedValue);
            	}else{
            		List<String> list=new ArrayList<String>();
            		list.add(formattedValue);
            		rowMap.put(new Integer(currentRow), list);
            	}
            }
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
            // Skip, no headers or footers in CSV
        }

    }

    /**
     * Creates a new XLSX -> CSV converter
     *
     * @param pkg        The XLSX package to process
     * @param output     The PrintStream to output the CSV to
     * @param minColumns The minimum number of columns to output, or -1 for no minimum
     
    public XLSX2CSV(OPCPackage pkg, PrintStream output, int minColumns) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
    }*/
    
    public XLSX2CSV(OPCPackage pkg) {
        this.xlsxPackage = pkg;
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles
     * @param strings
     * @param sheetInputStream
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            SheetContentsHandler sheetHandler,
            InputStream sheetInputStream)
            throws IOException, ParserConfigurationException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new FormXSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void process()
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
//        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
//            String sheetName = iter.getSheetName();
            processSheet(styles, strings, new SheetToCSV(), stream);
            stream.close();
//            ++index;
        }
    }

	public void setAttrInfoList(List<CustomFormAttribute> attrInfoList) {
		this.attrInfoList = attrInfoList;
	}

	public List<CustomFormAttribute> getAttrInfoList() {
		return attrInfoList;
	}

	public ConcurrentHashMap<Integer, List<String>> getRowMap() {
		return rowMap;
	}

    
}