using System;
using System.Web.Services;
using System.Collections.Generic;
using System.Data.SqlClient;

namespace ServicioSegip
{
    [Serializable]
    public class Persona
    {
        public int Id;
        public string CI;
        public string Nombres;
        public string PrimerApellido;
        public string SegundoApellido;
    }

    [WebService(Namespace = "http://servicios.segip.bo/")]
    public class SegipService : WebService
    {
        private string conn = "Server=.;Database=PersonasDB;Integrated Security=True;";

        [WebMethod]
        public Persona BuscarPersonaCI(string numeroDocumento)
        {
            using (SqlConnection cn = new SqlConnection(conn))
            {
                cn.Open();
                SqlCommand cmd = new SqlCommand("SELECT id, ci, nombres, primer_apellido, segundo_apellido FROM Personas WHERE ci=@ci", cn);
                cmd.Parameters.AddWithValue("@ci", numeroDocumento);
                SqlDataReader rd = cmd.ExecuteReader();
                if (rd.Read())
                {
                    Persona p = new Persona();
                    p.Id = rd.GetInt32(0);
                    p.CI = rd.GetString(1);
                    p.Nombres = rd.GetString(2);
                    p.PrimerApellido = rd.GetString(3);
                    p.SegundoApellido = rd.GetString(4);
                    return p;
                }
                return null;
            }
        }

        [WebMethod]
        public Persona[] BuscarPersonas(string primerApellido, string segundoApellido, string nombres)
        {
            List<Persona> lista = new List<Persona>();
            using (SqlConnection cn = new SqlConnection(conn))
            {
                cn.Open();
                SqlCommand cmd = new SqlCommand("SELECT id, ci, nombres, primer_apellido, segundo_apellido FROM Personas WHERE primer_apellido=@pa OR segundo_apellido=@sa OR nombres=@nom", cn);
                cmd.Parameters.AddWithValue("@pa", primerApellido);
                cmd.Parameters.AddWithValue("@sa", segundoApellido);
                cmd.Parameters.AddWithValue("@nom", nombres);
                SqlDataReader rd = cmd.ExecuteReader();
                while (rd.Read())
                {
                    Persona p = new Persona();
                    p.Id = rd.GetInt32(0);
                    p.CI = rd.GetString(1);
                    p.Nombres = rd.GetString(2);
                    p.PrimerApellido = rd.GetString(3);
                    p.SegundoApellido = rd.GetString(4);
                    lista.Add(p);
                }
            }
            return lista.ToArray();
        }
    }
}
