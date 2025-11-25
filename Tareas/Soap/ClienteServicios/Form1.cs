using System;
using System.Drawing;
using System.Windows.Forms;
using ClienteServicios.CotizacionRef;
using ClienteServicios.SegipRef;

namespace ClienteServicios
{
    public class Form1 : Form
    {
        private TextBox txtFechaCot;
        private TextBox txtMontoCot;
        private Button btnObtenerCot;
        private Button btnRegistrarCot;
        private Label lblResultadoCot;

        private TextBox txtCISegip;
        private Button btnBuscarCI;
        private TextBox txtPASegip;
        private TextBox txtSASegip;
        private TextBox txtNomSegip;
        private Button btnBuscarPersonas;
        private ListBox lstPersonas;

        public Form1()
        {
            InitializeComponent();
        }

        private void InitializeComponent()
        {
            this.Text = "Cliente Servicios SOAP";
            this.Size = new Size(800, 450);

            Label lbl1 = new Label();
            lbl1.Text = "Servicio Cotizacion";
            lbl1.Location = new Point(20, 20);
            lbl1.AutoSize = true;
            this.Controls.Add(lbl1);

            Label lblFecha = new Label();
            lblFecha.Text = "Fecha:";
            lblFecha.Location = new Point(20, 50);
            lblFecha.AutoSize = true;
            this.Controls.Add(lblFecha);

            txtFechaCot = new TextBox();
            txtFechaCot.Location = new Point(80, 47);
            txtFechaCot.Width = 100;
            this.Controls.Add(txtFechaCot);

            Label lblMonto = new Label();
            lblMonto.Text = "Monto:";
            lblMonto.Location = new Point(20, 80);
            lblMonto.AutoSize = true;
            this.Controls.Add(lblMonto);

            txtMontoCot = new TextBox();
            txtMontoCot.Location = new Point(80, 77);
            txtMontoCot.Width = 100;
            this.Controls.Add(txtMontoCot);

            btnObtenerCot = new Button();
            btnObtenerCot.Text = "Obtener";
            btnObtenerCot.Location = new Point(200, 45);
            btnObtenerCot.Click += BtnObtenerCot_Click;
            this.Controls.Add(btnObtenerCot);

            btnRegistrarCot = new Button();
            btnRegistrarCot.Text = "Registrar";
            btnRegistrarCot.Location = new Point(200, 75);
            btnRegistrarCot.Click += BtnRegistrarCot_Click;
            this.Controls.Add(btnRegistrarCot);

            lblResultadoCot = new Label();
            lblResultadoCot.Text = "";
            lblResultadoCot.Location = new Point(20, 110);
            lblResultadoCot.AutoSize = true;
            this.Controls.Add(lblResultadoCot);

            Label lbl2 = new Label();
            lbl2.Text = "Servicio Segip";
            lbl2.Location = new Point(20, 160);
            lbl2.AutoSize = true;
            this.Controls.Add(lbl2);

            Label lblCI = new Label();
            lblCI.Text = "CI:";
            lblCI.Location = new Point(20, 190);
            lblCI.AutoSize = true;
            this.Controls.Add(lblCI);

            txtCISegip = new TextBox();
            txtCISegip.Location = new Point(80, 187);
            txtCISegip.Width = 100;
            this.Controls.Add(txtCISegip);

            btnBuscarCI = new Button();
            btnBuscarCI.Text = "Buscar CI";
            btnBuscarCI.Location = new Point(200, 185);
            btnBuscarCI.Click += BtnBuscarCI_Click;
            this.Controls.Add(btnBuscarCI);

            Label lblPA = new Label();
            lblPA.Text = "Primer Apellido:";
            lblPA.Location = new Point(20, 220);
            lblPA.AutoSize = true;
            this.Controls.Add(lblPA);

            txtPASegip = new TextBox();
            txtPASegip.Location = new Point(130, 217);
            txtPASegip.Width = 100;
            this.Controls.Add(txtPASegip);

            Label lblSA = new Label();
            lblSA.Text = "Segundo Apellido:";
            lblSA.Location = new Point(20, 250);
            lblSA.AutoSize = true;
            this.Controls.Add(lblSA);

            txtSASegip = new TextBox();
            txtSASegip.Location = new Point(130, 247);
            txtSASegip.Width = 100;
            this.Controls.Add(txtSASegip);

            Label lblNom = new Label();
            lblNom.Text = "Nombres:";
            lblNom.Location = new Point(20, 280);
            lblNom.AutoSize = true;
            this.Controls.Add(lblNom);

            txtNomSegip = new TextBox();
            txtNomSegip.Location = new Point(130, 277);
            txtNomSegip.Width = 150;
            this.Controls.Add(txtNomSegip);

            btnBuscarPersonas = new Button();
            btnBuscarPersonas.Text = "Buscar Personas";
            btnBuscarPersonas.Location = new Point(300, 275);
            btnBuscarPersonas.Click += BtnBuscarPersonas_Click;
            this.Controls.Add(btnBuscarPersonas);

            lstPersonas = new ListBox();
            lstPersonas.Location = new Point(20, 310);
            lstPersonas.Size = new Size(740, 80);
            this.Controls.Add(lstPersonas);
        }

        private void BtnObtenerCot_Click(object sender, EventArgs e)
        {
            CotizacionServiceSoapClient cli = new CotizacionServiceSoapClient();
            string resultado = cli.obtenerCotizacion(txtFechaCot.Text);
            lblResultadoCot.Text = resultado;
            cli.Close();
        }

        private void BtnRegistrarCot_Click(object sender, EventArgs e)
        {
            CotizacionServiceSoapClient cli = new CotizacionServiceSoapClient();
            decimal monto;
            if (decimal.TryParse(txtMontoCot.Text, out monto))
            {
                string resultado = cli.registrarCotizacion(txtFechaCot.Text, monto);
                lblResultadoCot.Text = resultado;
            }
            else
            {
                lblResultadoCot.Text = "Monto invalido";
            }
            cli.Close();
        }

        private void BtnBuscarCI_Click(object sender, EventArgs e)
        {
            SegipServiceSoapClient cli = new SegipServiceSoapClient();
            Persona p = cli.BuscarPersonaCI(txtCISegip.Text);
            lstPersonas.Items.Clear();
            if (p != null)
            {
                lstPersonas.Items.Add(p.CI + " - " + p.Nombres + " " + p.PrimerApellido + " " + p.SegundoApellido);
            }
            else
            {
                lstPersonas.Items.Add("No se encontro la persona");
            }
            cli.Close();
        }

        private void BtnBuscarPersonas_Click(object sender, EventArgs e)
        {
            SegipServiceSoapClient cli = new SegipServiceSoapClient();
            Persona[] personas = cli.BuscarPersonas(txtPASegip.Text, txtSASegip.Text, txtNomSegip.Text);
            lstPersonas.Items.Clear();
            if (personas != null && personas.Length > 0)
            {
                foreach (Persona p in personas)
                {
                    lstPersonas.Items.Add(p.CI + " - " + p.Nombres + " " + p.PrimerApellido + " " + p.SegundoApellido);
                }
            }
            else
            {
                lstPersonas.Items.Add("No se encontraron personas");
            }
            cli.Close();
        }
    }
}
