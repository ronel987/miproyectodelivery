$(function () {
    let nombres = sessionStorage.getItem('nombres');
    let apellidos = sessionStorage.getItem('apellidos');
    let idusuario = sessionStorage.getItem('idusuario');
    
    let nom = nombres.split(" ");
    let ape = apellidos.split(" ");
    
    $("#user_log").text(nom[0] + " " + ape[0]);
    $("#user_idusuario").val(idusuario);
    
    ahora();
});

function ahora() {
    var dia = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
    var t = new Date();
    var dd = t.getDate() < 10 ? "0" + t.getDate() : t.getDate();
    var mes = eval(t.getMonth()) + 1;
    var MM = mes < 10 ? "0" + mes : mes;
    var yy = t.getFullYear();
    var hh = t.getHours() < 10 ? "0" + t.getHours() : t.getHours();
    var mm = t.getMinutes() < 10 ? "0" + t.getMinutes() : t.getMinutes();

    var hoy = dia[t.getDay()] + ", "
            + dd + "/" + MM + "/" + yy
            + " " + hh + ":" + mm;

    //return ahora;
    $("#user_ahora").text(hoy);
    setTimeout(ahora, 1000);
}