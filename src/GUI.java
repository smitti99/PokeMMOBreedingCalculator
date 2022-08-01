import jdk.jshell.spi.ExecutionControl;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class GUI {
    BreedingData Model;
    JFrame frame = new JFrame("Pokemon Breeding Calculator");
    JPanel BreedInformation_IVs = new JPanel();
    JPanel BreedInformation_Setup = new JPanel();
    JPanel MainPanel = new JPanel();
    JButton addButton = new JButton("+");
    Component bottomFiller = Box.createGlue();
    GridBagLayout BreedInfoLayout_Grid = new GridBagLayout();
    BoxLayout BreedInfoLayout_Box = new BoxLayout(BreedInformation_Setup, BoxLayout.Y_AXIS);
    JCheckBox IsGenderLessInput = new JCheckBox("Genderless Pokemon?");
    JCheckBox NaturedInput = new JCheckBox("Nature? ");
    LinkedList<Component> NoGenderDisableList = new LinkedList<>();
    LinkedList<Component> NaturedPokemonDisableList = new LinkedList<>();
    LinkedList<IVRow> IVRows = new LinkedList<>();

    int rows = 1;

    public GUI() {
        Model = new BreedingData();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (rows > 5) frame.setMinimumSize(new Dimension(440, 350 + (rows - 5) * 32));
                addIVRow(BreedInformation_IVs, BreedInfoLayout_Grid);
            }
        });
        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setFont(new Font("Arial", Font.PLAIN, 15));
        //Creating the Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(650, 360));

        MainPanel.setLayout(new BoxLayout(MainPanel, BoxLayout.Y_AXIS));
        //creating the BreedingInformationPanel
        BreedInformation_Setup.setLayout(BreedInfoLayout_Box);
        Box GenderLess = Box.createHorizontalBox();
        GenderLess.add(Box.createHorizontalStrut(10));
        GenderLess.add(IsGenderLessInput);
        GenderLess.add(Box.createHorizontalGlue());
        IsGenderLessInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.hasGender = itemEvent.getStateChange() != ItemEvent.SELECTED;
                updateGenderDependentUI();
            }
        });
        BreedInformation_Setup.add(GenderLess, BreedInfoLayout_Box);
        Box NaturedPanel = Box.createHorizontalBox();
        NaturedPanel.add(Box.createHorizontalStrut(10));
        NaturedPanel.add(NaturedInput, new BoxLayout(NaturedPanel, BoxLayout.X_AXIS));
        NaturedInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[6] = itemEvent.getStateChange() == ItemEvent.SELECTED;
                updateGenderDependentUI();
            }
        });
        Box NatureGender = Box.createHorizontalBox();
        JCheckBox NaturedMale = new JCheckBox("♂:");
        NaturedMale.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Stats1[6].male = (itemEvent.getStateChange() == ItemEvent.SELECTED) ? 1 : 0;
            }
        });
        NaturedMale.setHorizontalTextPosition(SwingConstants.LEADING);
        NatureGender.add(NaturedMale, new BoxLayout(NatureGender, BoxLayout.X_AXIS));
        NaturedPokemonDisableList.add(NaturedMale);
        JCheckBox female = new JCheckBox("♀:");
        female.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Stats1[6].female = (itemEvent.getStateChange() == ItemEvent.SELECTED) ? 1 : 0;
            }
        });
        NaturedPokemonDisableList.add(female);
        female.setHorizontalTextPosition(SwingConstants.LEADING);
        NatureGender.add(female, new BoxLayout(NatureGender, BoxLayout.X_AXIS));
        NaturedPanel.add(NatureGender, new BoxLayout(NaturedPanel, BoxLayout.X_AXIS));
        NaturedPanel.add(Box.createHorizontalGlue());

        BreedInformation_Setup.add(NaturedPanel, BreedInfoLayout_Box);

        MainPanel.add(BreedInformation_Setup);

        BreedInformation_IVs.setMaximumSize(new Dimension(120, 30));
        setupGridLayout(BreedInformation_IVs, BreedInfoLayout_Grid);
        SetupGridLabels(BreedInformation_IVs, BreedInfoLayout_Grid);
        addComponentToGrid(BreedInformation_IVs, BreedInfoLayout_Grid, addButton, 0, 1, 1, 1, new Insets(5, 5, 5, 5));
        addComponentToGrid(BreedInformation_IVs, BreedInfoLayout_Grid, bottomFiller, 0, 3, 1, 1, new Insets(5, 5, 5, 5));


        addIVRow(BreedInformation_IVs, BreedInfoLayout_Grid);
        addIVRow(BreedInformation_IVs, BreedInfoLayout_Grid);
        MainPanel.add(BreedInformation_IVs);

        //Creating the panel at bottom
        JPanel Footer = new JPanel();
        JButton calculateBTN = new JButton("Calculate");
        calculateBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (IVRow row : IVRows) {
                    String maleField = row.Counts[0].getText();
                    String femaleField = row.Counts[1].getText();
                    int maleCount = 0;
                    int femaleCount = 0;

                    if (!maleField.isBlank())
                        maleCount = Integer.parseInt(maleField);
                    if (!femaleField.isBlank())
                        femaleCount = Integer.parseInt(femaleField);

                    LinkedList<Integer> indices = new LinkedList<Integer>();
                    for (int i = 0; i < 6; i++) {
                        if (row.Stats[i].isSelected()) indices.add(i);
                    }
                    if (indices.size() == 0) continue;
                    Model.has[indices.size() - 1] = true;
                    if (Model.Goal[6]) Model.has[0] = true;
                    switch (indices.size()) {
                        case 1:
                            Model.Stats1[indices.get(0)].male += maleCount;
                            Model.Stats1[indices.get(0)].female += femaleCount;
                            break;
                        case 2:
                            Model.Stats2[indices.get(0)][indices.get(1)].male += maleCount;
                            Model.Stats2[indices.get(0)][indices.get(1)].female += femaleCount;
                            break;
                        case 3:
                            Model.Stats3[indices.get(0)][indices.get(1)][indices.get(2)].male += maleCount;
                            Model.Stats3[indices.get(0)][indices.get(1)][indices.get(2)].female += femaleCount;
                            break;
                        case 4:
                            Model.Stats4[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)].male += maleCount;
                            Model.Stats4[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)].female += femaleCount;
                            break;
                        case 5:
                            Model.Stats5[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)][indices.get(4)].male += maleCount;
                            Model.Stats5[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)][indices.get(4)].female += femaleCount;
                            break;
                        case 6:
                            Model.Stats6[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)][indices.get(4)][indices.get(5)].male += maleCount;
                            Model.Stats6[indices.get(0)][indices.get(1)][indices.get(2)][indices.get(3)][indices.get(4)][indices.get(5)].female += femaleCount;
                            break;

                        default:
                            break;
                    }
                }
                CalculateTree();
            }
        });
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();
                main.StartProgram();
            }
        });
        Footer.add(calculateBTN);
        Footer.add(reset);


        updateGenderDependentUI();
        //Adding Components to the frame.#
        frame.getContentPane().add(BorderLayout.NORTH, BreedInformation_Setup);
        frame.getContentPane().add(BorderLayout.CENTER, MainPanel);
        frame.getContentPane().add(BorderLayout.CENTER, BreedInformation_IVs);
        frame.getContentPane().add(BorderLayout.SOUTH, Footer);
        frame.setVisible(true);
    }

    private void updateGenderDependentUI() {
        boolean visible = Model.hasGender;
        for (Component c : NoGenderDisableList) {
            c.setVisible(visible);
        }
        visible = visible && Model.Goal[6];
        for (Component c : NaturedPokemonDisableList) {
            c.setVisible(visible);
        }

    }

    private void SetupGridLabels(JPanel panel, GridBagLayout gbl) {
        int i = 0;
        JCheckBox HPCheck = new JCheckBox("HP");
        JCheckBox AtkCheck = new JCheckBox("Atk");
        JCheckBox DefCheck = new JCheckBox("Def");
        JCheckBox SAtkCheck = new JCheckBox("SAtk");
        JCheckBox SDefCheck = new JCheckBox("SDef");
        JCheckBox InitCheck = new JCheckBox("Init");
        JLabel FemaleCount = new JLabel("Count ♀");
        NoGenderDisableList.add(FemaleCount);
        JLabel MaleLabel = new JLabel("                    ♂");
        NoGenderDisableList.add(MaleLabel);
        addComponentToGrid(panel, gbl, HPCheck, i++, 0, 1, 1, new Insets(10, 10, 5, 0));
        addComponentToGrid(panel, gbl, AtkCheck, i++, 0, 1, 1, new Insets(10, 30, 5, 0));
        addComponentToGrid(panel, gbl, DefCheck, i++, 0, 1, 1, new Insets(10, 30, 5, 0));
        addComponentToGrid(panel, gbl, SAtkCheck, i++, 0, 1, 1, new Insets(10, 38, 5, 0));
        addComponentToGrid(panel, gbl, SDefCheck, i++, 0, 1, 1, new Insets(10, 39, 5, 0));
        addComponentToGrid(panel, gbl, InitCheck, i++, 0, 1, 1, new Insets(10, 29, 5, 0));
        addComponentToGrid(panel, gbl, new JLabel("Count    "), i++, 0, 1, 1, new Insets(5, 37, 0, 0));
        addComponentToGrid(panel, gbl, MaleLabel, i - 1, 0, 1, 1, new Insets(5, 0, 0, 0));
        addComponentToGrid(panel, gbl, FemaleCount, i++, 0, 1, 1, new Insets(5, 7, 0, 0));
        addFillerComponentToGrid(panel, gbl, i, 0, true);

        HPCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[0] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
        AtkCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[1] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
        DefCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[2] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
        SAtkCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[3] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
        SDefCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[4] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
        InitCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                Model.Goal[5] = itemEvent.getStateChange() == ItemEvent.SELECTED;
            }
        });
    }

    private void addIVRow(JPanel panel, GridBagLayout gbl) {
        IVRow rowStruct = new IVRow();
        JTextField maleCount = new JTextField(4);
        rowStruct.Counts[0] = maleCount;
        maleCount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });

        JTextField femaleCount = new JTextField(4);
        rowStruct.Counts[1] = femaleCount;
        femaleCount.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();  // if it's not a number, ignore the event
                }
            }
        });
        NoGenderDisableList.add(femaleCount);
        int j = 0;
        JCheckBox hpCheck = new JCheckBox();
        rowStruct.Stats[j++] = hpCheck;
        hpCheck.setOpaque(false);
        JCheckBox atkCheck = new JCheckBox();
        rowStruct.Stats[j++] = atkCheck;
        atkCheck.setOpaque(false);
        JCheckBox defCheck = new JCheckBox();
        rowStruct.Stats[j++] = defCheck;
        defCheck.setOpaque(false);
        JCheckBox sAtkCheck = new JCheckBox();
        rowStruct.Stats[j++] = sAtkCheck;
        sAtkCheck.setOpaque(false);
        JCheckBox sDefCheck = new JCheckBox();
        rowStruct.Stats[j++] = sDefCheck;
        sDefCheck.setOpaque(false);
        JCheckBox InitCheck = new JCheckBox();
        rowStruct.Stats[j++] = InitCheck;
        InitCheck.setOpaque(false);

        int i = 0;
        addComponentToGrid(panel, gbl, hpCheck, i++, rows, 1, 1, new Insets(5, -12, 5, 0));
        addComponentToGrid(panel, gbl, atkCheck, i++, rows, 1, 1, new Insets(5, 5, 5, 0));
        addComponentToGrid(panel, gbl, defCheck, i++, rows, 1, 1, new Insets(5, 5, 5, 0));
        addComponentToGrid(panel, gbl, sAtkCheck, i++, rows, 1, 1, new Insets(5, 5, 5, 0));
        addComponentToGrid(panel, gbl, sDefCheck, i++, rows, 1, 1, new Insets(5, 5, 5, 0));
        addComponentToGrid(panel, gbl, InitCheck, i++, rows, 1, 1, new Insets(5, 5, 5, 0));
        addComponentToGrid(panel, gbl, maleCount, i++, rows, 1, 1, new Insets(5, 35, 5, 5));
        addComponentToGrid(panel, gbl, femaleCount, i++, rows, 1, 1, new Insets(5, 5, 5, 5));
        addFillerComponentToGrid(panel, gbl, i, rows, true);
        rows++;

        panel.remove(addButton);
        addComponentToGrid(panel, gbl, addButton, 0, rows, 1, 1, new Insets(5, -7, 5, 5));
        panel.remove(bottomFiller);
        bottomFiller = addFillerComponentToGrid(panel, gbl, 0, rows + 1, false);
        IVRows.add(rowStruct);

        updateGenderDependentUI();
        panel.revalidate();
        panel.repaint();
    }


    private void setupGridLayout(JPanel panel, GridBagLayout layout) {
        layout.columnWidths = new int[]{0, 0, 0, 0};
        layout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        layout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(layout);
    }

    void addComponentToGrid(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height, Insets padding) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = padding;
        gbl.setConstraints(c, gbc);
        cont.add(c);
    }

    void addComponentToGrid(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height) {
        addComponentToGrid(cont, gbl, c, x, y, width, height, new Insets(0, 0, 0, 0));
    }

    Component addFillerComponentToGrid(Container cont, GridBagLayout gbl, int last, int row, boolean horizontalFiller) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = last;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = horizontalFiller ? 1 : 0;
        gbc.weighty = horizontalFiller ? 0 : 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        Component c = Box.createGlue();
        gbl.setConstraints(c, gbc);
        cont.add(c);
        return c;
    }

    private void CalculateTree() {
        int num = 0;
        BreedingData result;

        for (boolean s : Model.Goal) {
            if (s) num++;
        }

        if (Model.hasGender)
            result = main.CreateBreedingTreeGender(Model, num);
        else result = main.CreateBreedingTree(Model, num);

        System.out.println(result.success);
        ShowResult(result.root);
    }

    private void ShowResult(TreeNode result) {
    }
}

class IVRow {
    JCheckBox[] Stats = new JCheckBox[6];
    JTextField[] Counts = new JTextField[2];
}