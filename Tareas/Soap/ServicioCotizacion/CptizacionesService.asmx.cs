using System;
using System.Web.Services;
using System.Data.SqlClient;

namespace ServicioCotizacion
{
    [WebService(Namespace = "http://servicios.universidad.bo/")]
    public class CotizacionService : WebService
    {
        private string conn = "Server=.;Database=CotizacionesDB;Integrated Security=True;";

        [WebMethod]
        public string obtenerCotizacion(string fecha)
        {
            using (SqlConnection cn = new SqlConnection(conn))
            {
                cn.Open();
                SqlCommand cmd = new SqlCommand("SELECT cotizacion, cotizacion_oficial FROM Cotizaciones WHERE fecha=@f", cn);
                cmd.Parameters.AddWithValue("@f", fecha);
                SqlDataReader rd = cmd.ExecuteReader();
                if (rd.Read())
                {
                    return "Cotizacion: " + rd[0].ToString() + " | Oficial: " + rd[1].ToString();
                }
                return "No existe cotizacion para esa fecha";
            }
        }

        [WebMethod]
        public string registrarCotizacion(string fecha, decimal monto)
        {
            using (SqlConnection cn = new SqlConnection(conn))
            {
                cn.Open();
                SqlCommand cmd = new SqlCommand("INSERT INTO Cotizaciones(fecha, cotizacion, cotizacion_oficial) VALUES (@f, @c, '6.97')", cn);
                cmd.Parameters.AddWithValue("@f", fecha);
                cmd.Parameters.AddWithValue("@c", monto);
                try
                {
                    cmd.ExecuteNonQuery();
                    return "Cotizacion registrada con exito";
                }
                catch
                {
                    return "Error al registrar cotizacion";
                }
            }
        }
    }
}
