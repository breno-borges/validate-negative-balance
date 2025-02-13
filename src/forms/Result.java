/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package forms;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import validatenegativebalance.GetColumnValue;
import validatenegativebalance.ResultField;
import validatenegativebalance.Validation;

/**
 *
 * @author breno.santos.o2b_dot
 */
public class Result extends javax.swing.JDialog {

    /**
     * Creates new form Result
     */
    public Result(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private String filePath;

    /**
     * Creates new form Result
     *
     * @param parent
     * @param modal
     * @param filePath
     * @throws java.io.IOException
     */
    public Result(java.awt.Frame parent, boolean modal, String filePath) throws IOException {
        super(parent, modal);
        initComponents();
        this.filePath = filePath;
        result();
    }

    private void result() throws IOException {
        List<Double> listAcumulate = GetColumnValue.getColumnAcumuloValues(filePath);

        if (listAcumulate.get(0) == 0) {
            textReasons.setText("Saldo já está zerado");
            textActions.setText("Nenhuma ação necessária");
            textFor.setText("Nenhuma ação necessária");
            textTransactions.setText("Saldo já está zerado");
            textFile500.setText("Saldo já está zerado");
            textCurrentylBalance.setText(listAcumulate.get(0).toString());
            textNewBalance.setText("Não aplicável");
        } else if (listAcumulate.get(0) > 0) {
            textReasons.setText("Saldo já é positivo");
            textActions.setText("Nenhuma ação necessária");
            textFor.setText("Nenhuma ação necessária");
            textTransactions.setText("Saldo já é positivo");
            textFile500.setText("Saldo já é positivo");
            textCurrentylBalance.setText(listAcumulate.get(0).toString());
            textNewBalance.setText("Não aplicável");
        } else {
            Validation validation = new Validation();
            List<ResultField> results = validation.validateNegativeBalance(filePath);

            if (results == null) {
                JOptionPane.showMessageDialog(null, "Situação não mapeada! Entre em contato com o desenvolvedor!");
            } else {
                StringBuilder operations = new StringBuilder();
                StringBuilder transactions = new StringBuilder();
                Set<String> uniqueOperations = new HashSet<>();
                double sumExpiredPoints = 0;
                Double newBalance;
                Double file500;

                for (ResultField result : results) {
                    uniqueOperations.add(result.getOperation());
                    if(result.getOperation().equals("EXPIRAÇÃO")
                        || result.getOperation().equals("VENCIMENTO")){
                        transactions.append(result.getTransaction()).append(", ");
                        sumExpiredPoints += (result.getPoint() * -1);
                    }
                }

                int i = 0;
                for (String operation : uniqueOperations) {
                    operations.append(operation);
                    if (i < uniqueOperations.size() - 1) { // Adiciona " e " apenas se não for o último elemento
                        operations.append(" e ");
                    }
                    i++;
                }

                // Remove , e espaços do final
                if (transactions.length() > 0) {
                    transactions.delete(transactions.length() - 2, transactions.length());
                }

                textTransactions.setText(transactions.toString());
                textReasons.setText(operations.toString());
                textActions.setText("Abrir um chamado para o time de operações");
                if (uniqueOperations.size() == 1 && uniqueOperations.contains("EXPIRAÇÃO") || uniqueOperations.contains("VENCIMENTO")){
                    textFor.setText("Reverter as transações abaixo");
                    textFile500.setText("Não se aplica");
                    newBalance = sumExpiredPoints + listAcumulate.get(0);
                    textNewBalance.setText(newBalance.toString());
                } else if(uniqueOperations.size() > 1 && uniqueOperations.contains("EXPIRAÇÃO") || uniqueOperations.contains("VENCIMENTO")) {
                    textFor.setText("Reverter as transações abaixo e enviar arquivo 500 para processar e bonificar o cliente");
                    file500 = listAcumulate.get(0) - (sumExpiredPoints + listAcumulate.get(0));
                    textFile500.setText(file500.toString());
                    newBalance = sumExpiredPoints + file500 + listAcumulate.get(0);
                    textNewBalance.setText(newBalance.toString());
                } else {
                    textFor.setText("Enviar arquivo 500 com o valor informado para processar e bonificar o cliente");
                    file500 = listAcumulate.get(0) * -1;
                    textFile500.setText(file500.toString());
                    textTransactions.setText("Não se aplica");
                    newBalance = file500 + listAcumulate.get(0);
                    textNewBalance.setText(newBalance.toString());
                }
                
                textCurrentylBalance.setText(listAcumulate.get(0).toString());
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelReasonsTitle = new javax.swing.JLabel();
        labelReasons = new javax.swing.JLabel();
        textReasons = new javax.swing.JTextField();
        separator1 = new javax.swing.JSeparator();
        labelActionTitle = new javax.swing.JLabel();
        labelActions = new javax.swing.JLabel();
        labelFor = new javax.swing.JLabel();
        labelTransactions = new javax.swing.JLabel();
        labelFile500 = new javax.swing.JLabel();
        textActions = new javax.swing.JTextField();
        textFor = new javax.swing.JTextField();
        textTransactions = new javax.swing.JTextField();
        textFile500 = new javax.swing.JTextField();
        separator2 = new javax.swing.JSeparator();
        labelBalanceTitle = new javax.swing.JLabel();
        labelCurrentlyBalance = new javax.swing.JLabel();
        labelNewBalance = new javax.swing.JLabel();
        textCurrentylBalance = new javax.swing.JTextField();
        textNewBalance = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Validação");
        setResizable(false);

        labelReasonsTitle.setText("Motivos do Saldo Ficar Negativo");

        labelReasons.setText("Motivos:");

        textReasons.setEditable(false);

        labelActionTitle.setText("Ações Recomendadas");

        labelActions.setText("Ações:");

        labelFor.setText("Para que:");

        labelTransactions.setText("Transações:");

        labelFile500.setText("Bonificar Em:");

        textActions.setEditable(false);

        textFor.setEditable(false);

        textTransactions.setEditable(false);

        textFile500.setEditable(false);

        labelBalanceTitle.setText("Saldo");

        labelCurrentlyBalance.setText("Saldo Atual:");

        labelNewBalance.setText("Saldo Após Ações:");

        textCurrentylBalance.setEditable(false);

        textNewBalance.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separator1)
            .addComponent(separator2)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(labelReasons)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textReasons, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelNewBalance)
                            .addComponent(labelCurrentlyBalance))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textCurrentylBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                            .addComponent(textNewBalance))))
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFile500, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelTransactions, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFor, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelActions, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textFile500)
                            .addComponent(textTransactions, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(textFor)
                            .addComponent(textActions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelActionTitle)
                        .addGap(232, 232, 232))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelReasonsTitle)
                        .addGap(217, 217, 217))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelBalanceTitle)
                        .addGap(267, 267, 267))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(labelReasonsTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textReasons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReasons))
                .addGap(18, 18, 18)
                .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelActionTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelActions)
                    .addComponent(textActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFor)
                    .addComponent(textFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textTransactions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelTransactions))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFile500, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelFile500))
                .addGap(18, 18, 18)
                .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelBalanceTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textCurrentylBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCurrentlyBalance))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textNewBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNewBalance))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Result.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Result.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Result.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Result.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Result dialog = new Result(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelActionTitle;
    private javax.swing.JLabel labelActions;
    private javax.swing.JLabel labelBalanceTitle;
    private javax.swing.JLabel labelCurrentlyBalance;
    private javax.swing.JLabel labelFile500;
    private javax.swing.JLabel labelFor;
    private javax.swing.JLabel labelNewBalance;
    private javax.swing.JLabel labelReasons;
    private javax.swing.JLabel labelReasonsTitle;
    private javax.swing.JLabel labelTransactions;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private javax.swing.JTextField textActions;
    private javax.swing.JTextField textCurrentylBalance;
    private javax.swing.JTextField textFile500;
    private javax.swing.JTextField textFor;
    private javax.swing.JTextField textNewBalance;
    private javax.swing.JTextField textReasons;
    private javax.swing.JTextField textTransactions;
    // End of variables declaration//GEN-END:variables
}
