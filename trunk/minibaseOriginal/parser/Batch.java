package parser;

import global.AttrType;
import global.GlobalConst;
import heap.FieldNumberOutOfBoundException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.SpaceNotAvailableException;
import heap.Tuple;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Batch implements GlobalConst {
	
	public void insert(Heapfile table,String fileName ){
		
		FileInputStream fstream = null;
		
		try {
			fstream = new FileInputStream(fileName);
			
			DataInputStream in = new DataInputStream(fstream);
			
			List<AttrType> columns = new ArrayList<AttrType>();
			List<Integer> tStringSizes = new ArrayList<Integer>();
			
			//Leemos la primera fila con la informacion sobre las columnas
			if(in.available() != 0){
				String line = in.readUTF();
				String[]  columnsFile = line.split("\t");
				
				for(String column: columnsFile){
					if(column.toLowerCase() == "integer"){
						columns.add(new AttrType(AttrType.attrInteger));
					}
					if(column.toLowerCase() == "real"){
						columns.add(new AttrType(AttrType.attrReal));
					}
					if(column.toLowerCase().startsWith("string")){
						columns.add(new AttrType(AttrType.attrString));
						tStringSizes.add(Integer.parseInt(column.substring(5)));		
					}
				}
			}
			
			short[] stringSizes = new short[tStringSizes.size()]; 
			
			//Como necesitamos un array de shorts para hacer feliz a setHdr convertimos nuestra lista de integer a una de shorts
			for(int i=0;i<stringSizes.length;i++){
				stringSizes[i] = (short)tStringSizes.get(i).intValue();
			}
			
			Tuple tuple = new Tuple();
			
			tuple.setHdr((short) columns.size(),(AttrType[]) columns.toArray(),stringSizes);
			//Como tuple funciona de una manera rara
			int size = tuple.size();
			tuple = new Tuple(size);
			tuple.setHdr((short) columns.size(),(AttrType[]) columns.toArray(),stringSizes);
			
			
			while (in.available() !=0) {
				String line = in.readUTF();
				String[] values = line.split("\t");
				
				for(int i=0;i<values.length;i++){
					
					switch(columns.get(i).attrType){
					
					case AttrType.attrInteger:
						tuple.setIntFld(i+1,Integer.parseInt(values[i]));
						break;
					case AttrType.attrReal:
						tuple.setFloFld(i+1,Float.parseFloat(values[i]));
						break;
					case AttrType.attrString:
						tuple.setStrFld(i+1,values[i]);
						break;
					}
					
				}
				
				table.insertRecord(tuple.returnTupleByteArray());
				tuple.print((AttrType[]) columns.toArray());
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTupleSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidSlotNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SpaceNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HFBufMgrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HFDiskMgrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Parsear el archivo con las tublas
	}
	
}
