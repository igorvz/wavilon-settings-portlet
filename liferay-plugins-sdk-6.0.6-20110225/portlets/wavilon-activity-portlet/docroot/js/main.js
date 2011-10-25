function showImgBox() {
    jQuery('.imgColumn').show();
    jQuery('.itemTimerTop').hide();
    jQuery('.itemTimerBottom').show();
    jQuery('.imgColumn').parent().parent().show();
//    jQuery('.mainItemContent').addClass("fullWidth");

    return false;
}

function hideImgBox() {
    jQuery('.imgColumn').hide();
    jQuery('.itemTimerTop').show();
    jQuery('.itemTimerBottom').hide();
    jQuery('.imgColumn').parent().parent().hide();
//    jQuery('.mainItemContent').addClass("fullWidth");

    return false;
}

function showChatBox() {
    jQuery('.chatBox').show();
//    jQuery('.imgColumn').parent().parent().show();
    return false;
}

function hideChatBox() {
    jQuery('.chatBox').hide();
//    jQuery('.imgColumn').parent().parent().hide();
    return false;
}