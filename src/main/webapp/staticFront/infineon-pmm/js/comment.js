function commentA(){
	var tags = [];
    var num=1;
    var postobj={
       textarea:null,
    }
    var $inputor=$('.inputor').atwho({
        at: "@",
        data: tags,
		searchKey:'nickname',
        limit: 4,
        displayTpl: "<li>${nickname}<span style='font-size: 28px;display:none' class='_fc1'>${id}</span></li>",
        insertTpl: "<span class='_fc3'>${atwho-at}${nickname}</span><i class='aaa' data-id='${id}'></i>",
        callbacks: {
            remoteFilter: function (query,callback) {
                $.getJSON("https://infineon-ipc.xind.cebest.com/center/user/getAllUserList", {name: query}, function(data) {
                    callback(data)
                });
            },
        }
    });
}