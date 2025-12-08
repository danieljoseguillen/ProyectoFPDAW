function sincronizarFiltro() {
    // Obtener los valores actuales del formulario de filtro
    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;
    const personas = document.getElementById('personas').value;
    
    // Actualizar los campos hidden del formulario de reserva
    document.getElementById('fechaInicioReserva').value = fechaInicio;
    document.getElementById('fechaFinReserva').value = fechaFin;
    document.getElementById('personasReserva').value = personas;
    
    // Permitir que el formulario se envíe
    return true;
}
