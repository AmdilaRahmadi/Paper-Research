import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class Gui {
    private JList<String> resultList;
    private JPanel panel1;
    private JPanel leftPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea descriptionArea;
    private boolean descriptionVisible = false; // Track visibility of description
    private boolean descriptionLocked = false; // Track if description is locked

    public Gui() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        // Panel kiri: History
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // Susunan vertikal
        leftPanel.setBorder(BorderFactory.createTitledBorder("History"));
        leftPanel.setPreferredSize(new Dimension(250, 0)); // Lebar default panel kiri

        // Scroll pane untuk panel kiri
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Tidak perlu scroll horizontal

        // Panel atas: Pencarian
        topPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchButton = new JButton("Search");
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        // Panel tengah: Hasil pencarian
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        resultList = new JList<>(new DefaultListModel<>());
        JScrollPane resultScrollPane = new JScrollPane(resultList);
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);

        // Panel kanan: Deskripsi
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Paper Description"));
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);

        // Button untuk hide/show deskripsi
        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(e -> toggleDescriptionVisibility());

        // Tombol Lock Description
        JButton lockButton = new JButton("Lock");
        lockButton.addActionListener(e -> {
            descriptionLocked = !descriptionLocked;
            lockButton.setText(descriptionLocked ? "Unlock" : "Lock");
        });

        // Tombol Open
        JButton openButton = new JButton("Buka");
        openButton.addActionListener(e -> {
            // Logic untuk open, misalnya membuka deskripsi di jendela baru
            JOptionPane.showMessageDialog(panel1, "Opening the paper...");
            // Bisa ganti dengan logika membuka file atau URL
        });

        // Tombol Download
        JButton downloadButton = new JButton("Unduh");
        downloadButton.addActionListener(e -> {
            // Logic untuk download (misalnya, menunjukkan pesan)
            JOptionPane.showMessageDialog(panel1, "Downloading the paper...");
        });

        // Tombol Share
        JButton shareButton = new JButton("Bagikan");
        shareButton.addActionListener(e -> {
            // Logic untuk share (misalnya, menunjukkan pesan)
            JOptionPane.showMessageDialog(panel1, "Sharing the paper...");
        });

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BorderLayout());
        rightTopPanel.add(hideButton, BorderLayout.WEST);
        rightTopPanel.add(lockButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(downloadButton);
        buttonPanel.add(shareButton);

        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH); // Panel button di bawah deskripsi
        rightPanel.setPreferredSize(new Dimension(0, 0)); // Default width set to 0, hidden initially

        // Menambahkan panel-panel ke panel utama
        panel1.add(leftScrollPane, BorderLayout.WEST);
        panel1.add(topPanel, BorderLayout.NORTH);
        panel1.add(centerPanel, BorderLayout.CENTER);
        panel1.add(rightPanel, BorderLayout.EAST);

        // Event pada tombol Search
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim();
                if (!query.isEmpty()) {
                    // Tambahkan entri baru ke panel kiri
                    JPanel historyItemPanel = createHistoryPanel(query);
                    leftPanel.add(historyItemPanel);
                    leftPanel.revalidate(); // Memperbarui tampilan setelah penambahan
                    leftPanel.repaint();

                    // Menambahkan hasil pencarian ke daftar
                    DefaultListModel<String> resultModel = (DefaultListModel<String>) resultList.getModel();
                    resultModel.clear();
                    resultModel.addElement("Paper 1 tentang " + query);
                    resultModel.addElement("Paper 2 tentang " + query);
                    resultModel.addElement("Paper 3 tentang " + query);
                }
            }
        });

        // Event pada list hasil pencarian
        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedPaper = resultList.getSelectedValue();
                if (selectedPaper != null) {
                    descriptionArea.setText(selectedPaper + "\n\nDeskripsi lengkap dari " + selectedPaper + ".");
                    slideInDescription(); // Panggil slide in saat hasil diklik
                }
            }
        });
    }

    // Membuat panel history dengan desain khusus
    private JPanel createHistoryPanel(String query) {
        JPanel historyPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(220, 220, 220));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };

        historyPanel.setBackground(new Color(255, 255, 255, 200));

        JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
        buttonPanel.setOpaque(false);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(new Color(150, 150, 150));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);

        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(Color.RED);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(new Color(150, 150, 150));
            }
        });

        closeButton.addActionListener(e -> {
            leftPanel.remove(historyPanel);
            leftPanel.revalidate();
            leftPanel.repaint();
        });

        buttonPanel.add(closeButton, BorderLayout.EAST);
        historyPanel.add(buttonPanel, BorderLayout.NORTH);

        JLabel label = new JLabel("<html><p style='width:180px; word-wrap: break-word;'>" + query + "</p></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        int labelHeight = label.getPreferredSize().height;

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(label, BorderLayout.CENTER);
        labelPanel.setPreferredSize(new Dimension(180, labelHeight));
        labelPanel.setOpaque(false);

        historyPanel.add(labelPanel, BorderLayout.CENTER);
        historyPanel.setPreferredSize(new Dimension(200, labelHeight + 30));
        historyPanel.setMaximumSize(new Dimension(200, labelHeight + 30));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateSearchResults(query);
                descriptionArea.setText(query + "\n\nDeskripsi lengkap dari " + query + ".");
                slideInDescription();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(100, 100, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLACK);
            }
        });

        return historyPanel;
    }

    // Memperbarui hasil pencarian
    private void updateSearchResults(String query) {
        DefaultListModel<String> resultModel = (DefaultListModel<String>) resultList.getModel();
        resultModel.clear();
        resultModel.addElement("Paper 1 tentang " + query);
        resultModel.addElement("Paper 2 tentang " + query);
        resultModel.addElement("Paper 3 tentang " + query);
    }

    // Animasi slide-in untuk panel deskripsi
    private void slideInDescription() {
        Timer timer = new Timer(10, new ActionListener() {
            int width = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (width < 300) {
                    width += 10;
                    rightPanel.setPreferredSize(new Dimension(width, rightPanel.getHeight()));
                    rightPanel.revalidate();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    // Menyembunyikan atau menampilkan deskripsi
    private void toggleDescriptionVisibility() {
        descriptionVisible = !descriptionVisible;
        rightPanel.setVisible(descriptionVisible);
    }

    public JPanel getPanel() {
        return panel1;
    }
}