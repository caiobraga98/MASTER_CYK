
package cyk_project;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class CYK_Run {

    private static Tabela_CYK[] tabelas;
    private static String[][] regras;
    private static Leitura_CYK ler;

    public static void main(String[] args) throws FileNotFoundException {
        File glc = new File("C:\\Users\\kaiob\\Documents\\NetBeansProjects\\CYK_LFA\\src\\glc.txt");
        File cadeias = new File("C:\\Users\\kaiob\\Documents\\NetBeansProjects\\CYK_LFA\\src\\cadeia.txt");
        ler = new Leitura_CYK(glc, cadeias);
        tabelas = new Tabela_CYK[ler.getCadeias().length];
        criarTabelas();
        regras = ler.getRegras();
        for (int i = 0; i < tabelas.length; i++) {
            cyk(tabelas[i]);
        }
        getStatus();
        Tabela();
    }

    private static void getStatus() throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("C:\\Users\\kaiob\\Documents\\NetBeansProjects\\CYK_LFA\\src\\status.txt")));
        for (int i = 0; i < tabelas.length; i++) {
            if (tabelas[i].aceita) {
                System.out.print("1 ");
            } else {
                System.out.print("0 ");
            }
        }
    }

    private static void Tabela() throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream("C:\\Users\\kaiob\\Documents\\NetBeansProjects\\CYK_LFA\\src\\out-tabela.txt")));

        System.out.println(tabelas.length);

        for (int k = 0; k < tabelas.length; k++) {
            System.out.println(tabelas[k].cadeia.replace("", " ").replaceFirst(" ", ""));
            if (!"&".equals(tabelas[k].cadeia)) {
                for (int i = 1; i < tabelas[k].tabela.length; i++) {
                    for (int j = i; j < tabelas[k].tabela.length; j++) {
                        String aux = tabelas[k].tabela[i][j];
                        System.out.println(i + " " + j + " " + aux.replaceFirst(" ", ""));
                    }
                }
            }
        }
    }

    public static void criarTabelas() {
        String[] cadeias = ler.getCadeias();
        for (int i = 0; i < cadeias.length; i++) {
            tabelas[i] = new Tabela_CYK(cadeias[i]);
        }
    }

    public static void cyk(Tabela_CYK tabela) {
        if ("&".equals(tabela.cadeia) && cadeiaVaziaEhRegra()) {
            tabela.aceita = true;
            return;
        }

        int n = tabela.tabela.length;
        for (int i = 1; i < n; i++) {
            String variavel = verificaMenorSub(tabela, i);
            if (variavel != null) {
                tabela.tabela[i][i] = variavel;
            }
        }
        for (int tamanhoSubcadeia = 2; tamanhoSubcadeia < n; tamanhoSubcadeia++) {
            for (int posInicial = 1; posInicial < n - tamanhoSubcadeia + 1; posInicial++) {
                int posFinal = posInicial + tamanhoSubcadeia - 1;
                for (int posDivisao = posInicial; posDivisao < posFinal; posDivisao++) {
                    String variavel = verificaSubcadeia(tabela, posInicial, posFinal, posDivisao);
                    if (!tabela.tabela[posInicial][posFinal].contains(variavel)) {
                        tabela.tabela[posInicial][posFinal] += variavel;
                    }
                }
            }
        }
        verificaAceitacao(tabela);
    }

    public static boolean cadeiaVaziaEhRegra() {
        for (int i = 0; i < regras.length; i++) {
            if (("&".equals(regras[i][2]))) {
                return true;
            }
        }
        return false;
    }

    private static void verificaAceitacao(Tabela_CYK tabela) {
        if (tabela.tabela[1][tabela.tabela.length - 1].contains(regras[0][0])) {
            tabela.aceita = true;
        } else {
            tabela.aceita = false;
        }
    }

    private static String verificaMenorSub(Tabela_CYK tabela, int caracterCadeia) {
        String resp = "";
        for (int i = 0; i < regras.length; i++) {
            if (tabela.tabela[0][caracterCadeia].equals(regras[i][2])) {
                resp = resp + " " + regras[i][0];
            }
        }
        return resp;
    }

    public static String verificaSubcadeia(Tabela_CYK tabela, int posInicial, int posFinal, int posDivisao) {
        String resp = "";
        for (int i = 0; i < regras.length; i++) {
            String b = regras[i][2];
            String c = regras[i][3];

            boolean first = tabela.tabela[posInicial][posDivisao].contains(b);
            boolean second = tabela.tabela[posDivisao + 1][posFinal].contains(c);
            if (first && second) {
                resp = resp + " " + regras[i][0];
            }
        }
        return resp;
    }

}
