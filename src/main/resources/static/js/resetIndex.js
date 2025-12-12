  const inicio = () => {
    document.getElementById("searchform").addEventListener("reset", resetearIndex);
}
document.addEventListener("DOMContentLoaded", inicio);

const indexValues = (e) => {
    // Campos de texto
    document.getElementById("nombre").value = "";
    document.getElementById("direccion").value = "";
    
    // Campos numéricos con mínimo
    document.getElementById("personas").value = "1";
    document.getElementById("cantHabi").value = "1";
    document.getElementById("presupuestoMin").value = "5";
    
    // Presupuesto máximo con valor máximo
    document.getElementById("presupuestoMax").value = "10000";
    
    // Fechas
    const hoy = new Date();
    const manana = new Date(hoy);
    manana.setDate(manana.getDate() + 1);
    
    document.getElementById("fechaInicio").value = formatoFecha(hoy);
    document.getElementById("fechaFin").value = formatoFecha(manana);
    
    // Selects con primer elemento
    document.getElementById("municipio").selectedIndex = 0;
    document.getElementById("filtro").selectedIndex = 0;
}

const formatoFecha = (fecha) => {
    const año = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${año}-${mes}-${dia}`;
}

const resetearIndex =(e)=> {
    if (e && typeof e.preventDefault === 'function') {
        
        try { e.preventDefault(); } catch (err) { /* ignore */ }
    }
    setTimeout(indexValues, 0);
}