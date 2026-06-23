package com.example.osornogourmet.data.remote

/**
 * Decodificador de polylines codificadas usando el Algoritmo de Polyline de Google.
 *
 * OpenRouteService retorna la geometría de las rutas como polylines codificadas.
 * Este objeto las decodifica a una lista de pares (latitud, longitud) que pueden
 * ser utilizados directamente con osmdroid para dibujar la ruta en el mapa.
 *
 * Referencia del algoritmo:
 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
 */
object PolylineDecoder {

    /**
     * Decodifica una polyline codificada a una lista de puntos geográficos.
     *
     * El algoritmo de Google codifica los puntos como diferencias respecto
     * al punto anterior, usando codificación de longitud variable en ASCII.
     *
     * NOTA: El resultado está en formato (latitud, longitud) para uso directo
     * con osmdroid. OpenRouteService entrega la geometría codificada que
     * internamente usa (lat, lng) en el algoritmo estándar de Google.
     *
     * @param encoded Cadena de polyline codificada.
     * @return Lista de pares (latitud, longitud) decodificados.
     */
    fun decode(encoded: String): List<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        var index = 0
        val length = encoded.length
        var lat = 0
        var lng = 0

        while (index < length) {
            // Decodificar latitud
            var shift = 0
            var resultBits = 0
            var byte: Int
            do {
                byte = encoded[index++].code - 63
                resultBits = resultBits or ((byte and 0x1F) shl shift)
                shift += 5
            } while (byte >= 0x20)
            val deltaLat = if (resultBits and 1 != 0) (resultBits shr 1).inv() else resultBits shr 1
            lat += deltaLat

            // Decodificar longitud
            shift = 0
            resultBits = 0
            do {
                byte = encoded[index++].code - 63
                resultBits = resultBits or ((byte and 0x1F) shl shift)
                shift += 5
            } while (byte >= 0x20)
            val deltaLng = if (resultBits and 1 != 0) (resultBits shr 1).inv() else resultBits shr 1
            lng += deltaLng

            // Convertir de enteros codificados a coordenadas decimales
            // El factor 1E5 es parte del algoritmo estándar de Google
            val latitude = lat / 1E5
            val longitude = lng / 1E5
            result.add(Pair(latitude, longitude))
        }

        return result
    }
}
