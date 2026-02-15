  const inicio = () => {
    document.getElementById("searchform").addEventListener("reset", resetearIndex);
}
document.addEventListener("DOMContentLoaded", inicio);

const indexValues = (e) => {
    // Campos de texto
    document.getElementById("nombre").value = "";
    document.getElementById("direccion").value = "";
    
    // Selects con primer elemento
    document.getElementById("municipio").selectedIndex = 0;
    document.getElementById("filtro").selectedIndex = 0;
}


const resetearIndex =(e)=> {
    if (e && typeof e.preventDefault === 'function') {
        
        try { e.preventDefault(); } catch (err) { /* ignore */ }
    }
    setTimeout(indexValues, 0);
}