function showImgBox() {
    jQuery('.imgColumn').show();
    jQuery('.imgColumn').parent().parent().show();
    jQuery('.infoLayout').parent().parent().height(150);
    return false;
}

function hideImgBox() {
    jQuery('.imgColumn').hide();
    jQuery('.imgColumn').parent().parent().hide();
    jQuery('.infoLayout').parent().parent().height(80);
    return false;
}

function showChatBox(){
    jQuery('.chatBox').show();
//    jQuery('.imgColumn').parent().parent().show();
    return false;
}

function hideChatBox(){
    jQuery('.chatBox').hide();
//    jQuery('.imgColumn').parent().parent().hide();
    return false;
}