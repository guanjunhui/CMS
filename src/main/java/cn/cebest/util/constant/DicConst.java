package cn.cebest.util.constant;

/**
 *字典表父级常量
 *@author qichanxin 
 */
public enum DicConst {

	Course("课程","002"),Area("地区","003"),Tuition("学费","004"),
	ScoolScale("学校规模","005"),Abroad("大学留学国家","006"),TemplateType("模板类型","009"),SEX("性别","007"),
	LocalLanguage("语言环境","010"),WorkNature("工作性质","A001"),EducationalRequirements("学历要求","A002"),
	SalaryRange("薪资范围","A003"),WorkExperience("工作年限","A004"),AgeRequirement("年龄要求","A005");
	private String name;
	private String bianma;
	
	private DicConst(String name,String bianma){
		this.name=name;
		this.bianma=bianma;
	}

	public String getName() {
		return name;
	}

	public String getBianma() {
		return bianma;
	}
	
}
