//si entras por url, te saca porque no tienes credenciales
$(function () {

    let token = sessionStorage.getItem('token');

    if (token === null || token === undefined || token === "") {
        window.location = "../";
    }

    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/autentica/valida",
        data: {
            token: token,
            autorizacion: "admin"
        },
        dataType: "json",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok === false) {
                window.location = "../";
            }
        }
    });
});

function salir() {
    let idusuario = sessionStorage.getItem("idusuario");

    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/autentica/exit",
        data: {
            idusuario: idusuario
        },
        dataType: "json",
        success: function (result) { // alert(JSON.stringify(result));

            if (result.ok === true) {
                sessionStorage.clear();
                
                window.location = "../";
            }
        }
    });
}
