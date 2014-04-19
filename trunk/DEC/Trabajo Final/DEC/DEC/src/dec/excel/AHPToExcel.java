/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dec.excel;

import dec.dominio.Problema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author PC ACER
 */
public class AHPToExcel extends ToExcel {
    private AHPRC AHPRCresolver;
    public AHPToExcel(Problema problema,File file) {
        super(problema, file);
        AHPRCresolver = new AHPRC();
    }

    @Override
    protected void header() {
        this.valores.add("Resolución por AHP");
      //  this.valores.add(super.problema.getP());
        this.libro.addRow(valores, RowType.HEADER);
    }


    @Override
    protected void detail() {
        //Normalizo la Matriz
        normalizeMatrix();
        /*Luego de tener la matriz normalizada
         * comprobamos la consistencia de los
         * juicios del desicisor utilizando el rc
         * empleando el calculo de IC A IA previamente
         */
        if(isNumberOfAltsGreaterThanTwo()){
            //Calculate RC
        }






        //Ponderamos la matriz
        
        valores.add("Valor de indice de consistencia");
        this.libro.addRow(valores, RowType.TITLE);

        this.libro.addEmptyRow();
        posValores.clear();
        this.libro.addRow(cabecera,RowType.HEADER);
        for (int i = 0; i < posValores2.size(); i++) {
            valores.clear();
            String[] fila = posValores2.get(i);
            valores.add("=T("+fila[0]+")");
            System.out.println();
            for (int j = 1; j < fila.length; j++) {
                System.out.print(fila[j]+" - ");
                valores.add("="+fila[j]+"*"+posPesos[j]);
            }
            posValores.add(this.libro.addRow(valores,RowType.CONTENT));
        }
        String primeraFila[] = posValores.get(0);
        String ultimaFila[] = posValores.get(posValores.size()-1);
        List ideal = new ArrayList();
        List antiIdeal = new ArrayList();
        ideal.add("A+");
        antiIdeal.add("A-");
        for (int i = 1; i < primeraFila.length; i++) {
            ideal.add("=MAX("+primeraFila[i]+":"+ultimaFila[i]+")");
            antiIdeal.add("=MIN("+primeraFila[i]+":"+ultimaFila[i]+")");
        }
        String posIdeal[] = this.libro.addRow(ideal,RowType.HEADER);
        String posAntiIdeal[] = this.libro.addRow(antiIdeal,RowType.HEADER);
        this.libro.addEmptyRows(2);

        //Calculamos las distacias
        valores.clear();
        valores.add("Cálculo de las Medidas de Distancia");
        this.libro.addRow(valores, RowType.TITLE);
        this.libro.addEmptyRow();

        valores.clear();
        valores.add("P");
        valores.add(1);//TODO problema.getP()
        String posP[] = this.libro.addRow(valores,RowType.CONTENT);

        List<String[]> distanciaIdeal = new ArrayList<String[]>();
        List<String[]> distanciaAntiIdeal = new ArrayList<String[]>();

        cabecera.add("S+");
        this.libro.addRow(cabecera,RowType.HEADER);
        for (int i = 0; i < posValores.size(); i++) {
            valores.clear();
            String[] fila = posValores.get(i);
            valores.add("=T("+fila[0]+")");
            for (int j = 1; j < fila.length; j++) {
                valores.add("=(ABS("+fila[j]+"-"+posIdeal[j]+"))^"+posP[1]);
            }
            String auxPos[] = this.libro.addRow(valores,RowType.CONTENT);
            String aux = this.libro.addCell("=(SUM("+auxPos[1]+":"+auxPos[auxPos.length-1]+"))^(1/"+posP[1]+")");
            auxPos = Arrays.copyOf(auxPos, auxPos.length+1);
            auxPos[auxPos.length-1] = aux;
            distanciaIdeal.add(auxPos);
        }
        cabecera.remove(cabecera.size()-1);
        cabecera.add("S-");
        this.libro.addRow(cabecera,RowType.HEADER);
        for (int i = 0; i < posValores.size(); i++) {
            valores.clear();
            String[] fila = posValores.get(i);
            valores.add("=T("+fila[0]+")");
            for (int j = 1; j < fila.length; j++) {
                valores.add("=(ABS("+fila[j]+"-"+posAntiIdeal[j]+"))^"+posP[1]);
            }
            String auxPos[] = this.libro.addRow(valores,RowType.CONTENT);
            String aux = this.libro.addCell("=(SUM("+auxPos[1]+":"+auxPos[auxPos.length-1]+"))^(1/"+posP[1]+")");
            auxPos = Arrays.copyOf(auxPos, auxPos.length+1);
            auxPos[auxPos.length-1] = aux;
            distanciaAntiIdeal.add(auxPos);
        }
        this.libro.addEmptyRows(2);

        //Proximidades relativas de cada alternativa

        valores.clear();
        valores.add("Cálculo de la Proximidad Relativa de Cada Alternativa a la Solución Ideal Positiva y Negativa");
        this.libro.addRow(valores, RowType.TITLE);
        this.libro.addEmptyRow();

        cabecera.clear();
        cabecera.add("Alternativas");
        cabecera.add("Ideal (S+)");
        cabecera.add("Anti Ideal (S-)");
        cabecera.add("Proximidad (C*)");
        this.libro.addRow(cabecera,RowType.HEADER);

        for (int i = 0; i < problema.getAlternativaList().size(); i++) {
            valores.clear();
            String[] disIdeal = distanciaIdeal.get(i);
            String[] disAntiIdeal = distanciaAntiIdeal.get(i);
            valores.add("=T("+disIdeal[0]+")");
            valores.add("="+disIdeal[disIdeal.length-1]);
            valores.add("="+disAntiIdeal[disAntiIdeal.length-1]);
            String auxPos[] = this.libro.addRow(valores,RowType.CONTENT);
            String aux = this.libro.addCell("="+auxPos[2]+"/("+auxPos[1]+"+"+auxPos[2]+")");
            auxPos = Arrays.copyOf(auxPos, auxPos.length+1);
            auxPos[auxPos.length-1] = aux;
        }

        for (int i = 1; i < this.problema.getCriterioList().size()+2; i++) {
            this.libro.autoSizeColumns(i);
        }
    }

    private void normalizeMatrix()
    {
        setNormMatrixHeader();
        setNormPesos();
        setNormValues();
    }

    private void setNormMatrixHeader(){
        //Seteamos el header de la matriz normalizada
        valores.clear();
        valores.add("Matrix Normalizada");
        this.libro.addRow(valores, RowType.TITLE);
    }

    private void setNormPesos(){
    //seteamos los pesos
        this.libro.addEmptyRow();
        pesos.clear();
        pesos.add("Pesos");
        for (int i = 1; i < posPesos.length-1; i++) {
            pesos.add("="+posPesos[i]+"/"+posPesos[posPesos.length-1]);
        }
        posPesos = this.libro.addRow(pesos,RowType.CONTENT);
    }

    private void setNormValues(){
     //seteamos los valores Normalizados
        this.libro.addRow(cabecera,RowType.HEADER);
        posValores2 = new ArrayList<String[]>();
        for (int i = 0; i < posValores.size(); i++) {
            valores.clear();
            String[] fila = posValores.get(i);
            valores.add("=T("+fila[0]+")");
            for (int j = 1; j < fila.length; j++) {
                valores.add("="+fila[j]+"/"+posTotales[j]);
            }
            posValores2.add(this.libro.addRow(valores,RowType.CONTENT));
        }
        this.libro.addEmptyRow();
        valores.clear();
    }

//    private double calculateRC(){
//        AHPRCresolver.resolveLamdaMax(getPesosCompMatrix());
//
//        for(int i = 0; i< 1 ; i++){
//
//        }
//    }
    
    private double[][] getPesosCompMatrix(){
        //Matriz cuadrada de comparacion de los pesos
        double [][] pesosCompMatrix = new double[pesos.size()][pesos.size()];
        for(int i=0; i< getNxNArraySize(pesos.size()) ;i++){
            pesosCompMatrix[i][getColumnIndex(i)]
                    = setCellMatrixValue(i,getColumnIndex(i));
        }
        return pesosCompMatrix;
    }

    private int getNxNArraySize(int size1XNvalue){
        return size1XNvalue* size1XNvalue;
    }

    private int getColumnIndex(int i){
        return pesos.size()-(pesos.size()-i);
    }

    private double setCellMatrixValue(int i,int j){
        return new Double(pesos.get(i).toString())
                /new Double(pesos.get(j).toString());
    }

    private double getIAValue(){
        //Si checkNumberOfAlts = true ejecutar esto
        int cant_Alternativas = problema.getAlternativaList().size();
        double AHPIAvalue=AHPRCresolver.getIAValue(cant_Alternativas);
        return AHPIAvalue;
    }

    private boolean isNumberOfAltsGreaterThanTwo()
    {
        boolean isAltsGreaterThanTwo = false;
        int cantidad_Alternativas = problema.getAlternativaList().size();
        //RC solo funciona con 3 alternativas en adelante
        if(cantidad_Alternativas>2)
        {
           isAltsGreaterThanTwo = true;
        }
        return isAltsGreaterThanTwo;
    }
}
