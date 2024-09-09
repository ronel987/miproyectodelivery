var ames = new Array('Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre');
var adia = new Array('Domingo', 'Lunes', 'Martes', 'Mi&eacute;rcoles', 'Jueves', 'Viernes', 'S&aacute;bado');

function getFecha() {
    var oDate = new Date();
    var dia = oDate.getDay();
    var mes = oDate.getMonth();
    var ndia = oDate.getDate();
    var anio = oDate.getFullYear();

    ndia = (ndia < 10) ? "0" + ndia : ndia;

    var fecha = adia[dia] + ", " + ndia + " de " + ames[mes] + " de " + anio;

    return fecha;
}

function getHora() {
    var oDate = new Date();
    hh = oDate.getHours();
    mm = oDate.getMinutes();
    ss = oDate.getSeconds();

    hh = (hh < 10) ? ("0" + hh) : hh;
    mm = (mm < 10) ? ("0" + mm) : mm;
    ss = (ss < 10) ? ("0" + ss) : ss;

    var hora = hh + ":" + mm + ":" + ss;

    return hora;
}
