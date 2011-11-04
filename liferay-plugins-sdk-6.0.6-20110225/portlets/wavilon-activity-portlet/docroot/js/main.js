function showImgBox() {
    jQuery('.imgColumn').show();
    jQuery('.itemTimerTop').hide();
    jQuery('.itemTimerBottom').show();
    jQuery('.imgColumn').parent().parent().css("width", "100px");
    jQuery('.imgColumn').parent().parent().css('margin', '0 ');

    return false;
}

function hideImgBox() {
    jQuery('.imgColumn').hide();
    jQuery('.itemTimerTop').show();
    jQuery('.itemTimerBottom').hide();
    jQuery('.imgColumn').parent().parent().css('margin', '0 -10px');
    jQuery('.imgColumn').parent().parent().css("width", "23px");

    return false;
}
