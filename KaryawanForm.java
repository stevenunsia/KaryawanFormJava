import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class KaryawanForm extends JFrame {
    private JTextField txtNik, txtNama, txtTempatLahir, txtTelepon, txtAlamat1, txtAlamat2;
    private JRadioButton rdLaki, rdPerempuan;
    private JComboBox<String> cbStatus;
    private JButton btnTampilkan, btnClear;
    private JSpinner dateSpinner;
    private JPanel mainPanel;
    private ButtonGroup bgGender;

    public KaryawanForm() {
        setTitle("Form Input Karyawan");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input NIK
        addComponent(new JLabel("NIK*:"), 0, 0, gbc);
        txtNik = new JTextField(20);
        txtNik.setToolTipText("Masukkan NIK Karyawan");
        addComponent(txtNik, 1, 0, gbc);

        // Input Nama
        addComponent(new JLabel("Nama*:"), 0, 1, gbc);
        txtNama = new JTextField(20);
        txtNama.setToolTipText("Masukkan Nama Lengkap");
        addComponent(txtNama, 1, 1, gbc);

        // Tempat dan Tanggal Lahir
        addComponent(new JLabel("TTL*:"), 0, 2, gbc);
        JPanel panelTtl = new JPanel(new GridLayout(1, 2, 5, 5));
        txtTempatLahir = new JTextField(10);
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        txtTempatLahir.setToolTipText("Masukkan Tempat Kelahiran");
        panelTtl.add(txtTempatLahir);
        panelTtl.add(dateSpinner);
        addComponent(panelTtl, 1, 2, gbc);

        // Jenis Kelamin
        JPanel panelGender = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdLaki = new JRadioButton("Laki-laki");
        rdPerempuan = new JRadioButton("Perempuan");
        bgGender = new ButtonGroup();
        bgGender.add(rdLaki);
        bgGender.add(rdPerempuan);
        panelGender.add(rdLaki);
        panelGender.add(rdPerempuan);
        
        addComponent(new JLabel("Jenis Kelamin:"), 0, 3, gbc);
        addComponent(panelGender, 1, 3, gbc);

        // Telepon dan Status
        addComponent(new JLabel("Telepon:"), 0, 4, gbc);
        txtTelepon = new JTextField(15);
        txtTelepon.setToolTipText("Contoh: 08123456789");
        addComponent(txtTelepon, 1, 4, gbc);

        addComponent(new JLabel("Status:"), 0, 5, gbc);
        String[] statusOptions = {"Belum Menikah", "Menikah"};
        cbStatus = new JComboBox<>(statusOptions);
        addComponent(cbStatus, 1, 5, gbc);

        // Alamat
        addComponent(new JLabel("Alamat 1:"), 0, 6, gbc);
        txtAlamat1 = new JTextField(30);
        txtAlamat1.setToolTipText("Masukkan Alamat Lengkap");
        addComponent(txtAlamat1, 1, 6, gbc);

        addComponent(new JLabel("Alamat 2:"), 0, 7, gbc);
        txtAlamat2 = new JTextField(30);
        txtAlamat2.setToolTipText("Masukkan Alamat Tambahan");
        addComponent(txtAlamat2, 1, 7, gbc);

        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTampilkan = new JButton("Tampilkan");
        btnTampilkan.addActionListener(e -> handleTampilkan());
        btnClear = new JButton("Bersihkan");
        btnClear.addActionListener(e -> clearForm());
        buttonPanel.add(btnClear);
        buttonPanel.add(btnTampilkan);
        
        gbc.gridwidth = 2;
        addComponent(buttonPanel, 0, 8, gbc);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }

    private void addComponent(Component comp, int x, int y, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        mainPanel.add(comp, gbc);
    }

    private void clearForm() {
        txtNik.setText("");
        txtNama.setText("");
        txtTempatLahir.setText("");
        txtTelepon.setText("");
        txtAlamat1.setText("");
        txtAlamat2.setText("");
        
        bgGender.clearSelection();
        cbStatus.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
    }

    private void handleTampilkan() {
        if (!validateInput()) return;
        
        JDialog resultDialog = createResultDialog();
        resultDialog.setVisible(true);
    }

    private boolean validateInput() {
        if (txtNik.getText().trim().isEmpty() || 
            txtNama.getText().trim().isEmpty() || 
            txtTempatLahir.getText().trim().isEmpty()) {
            
            showErrorDialog("Field bertanda * harus diisi!");
            return false;
        }
        return true;
    }

    private JDialog createResultDialog() {
        JDialog dialog = new JDialog(this, "Data Karyawan", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());

        JTextArea txtResult = new JTextArea(formatData());
        txtResult.setEditable(false);

        JButton btnSave = new JButton("Simpan");
        btnSave.addActionListener(e -> saveData(txtResult.getText(), dialog));

        dialog.add(new JScrollPane(txtResult), BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        
        return dialog;
    }

    private String formatData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tanggalLahir = sdf.format(dateSpinner.getValue());
        
        return String.format("""
            NIK: %s
            Nama: %s
            Tempat/Tgl Lahir: %s/%s
            Jenis Kelamin: %s
            Telepon: %s
            Status: %s
            Alamat 1: %s
            Alamat 2: %s""",
            txtNik.getText(),
            txtNama.getText(),
            txtTempatLahir.getText(),
            tanggalLahir,
            getSelectedGender(),
            txtTelepon.getText(),
            cbStatus.getSelectedItem(),
            txtAlamat1.getText(),
            txtAlamat2.getText()
        );
    }

    private String getSelectedGender() {
        if (rdLaki.isSelected()) return "Laki-laki";
        if (rdPerempuan.isSelected()) return "Perempuan";
        return "-";
    }

    private void saveData(String data, JDialog dialog) {
        String fileName = generateFileName();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
            showSuccessDialog(dialog, fileName);
            dialog.dispose();
            clearForm();
        } catch (IOException ex) {
            showErrorDialog("Gagal menyimpan data: " + ex.getMessage());
        }
    }

    private String generateFileName() {
        String nik = txtNik.getText().trim().replaceAll("\\s+", "_");
        String nama = txtNama.getText().trim().replaceAll("\\s+", "_");
        
        return String.format("%s_%s.txt", nik, nama);
    }

    private void showSuccessDialog(JDialog parent, String fileName) {
        JOptionPane.showMessageDialog(
            parent,
            "Data berhasil disimpan di:\n" + new File(fileName).getAbsolutePath(),
            "Sukses",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KaryawanForm());
    }
}