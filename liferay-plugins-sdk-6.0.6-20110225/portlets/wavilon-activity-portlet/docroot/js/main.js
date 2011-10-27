function showImgBox() {
    jQuery('.imgColumn').show();
    jQuery('.itemTimerTop').hide();
    jQuery('.itemTimerBottom').show();
    jQuery('.imgColumn').parent().parent().show();

    return false;
}

function hideImgBox() {
    jQuery('.imgColumn').hide();
    jQuery('.itemTimerTop').show();
    jQuery('.itemTimerBottom').hide();
    jQuery('.imgColumn').parent().parent().hide();

    return false;
}
