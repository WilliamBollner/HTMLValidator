package estrutura.ordenacao;

public class OrdenacaoMergeSort <T extends Comparable<T>> extends OrdenacaoAbstract<T> {
	
	public void ordenar() {
		int n = getInfo().length-1;
		mergeSort(0,n);
	}
	
	private void mergeSort(int inicio, int fim) {
		if(inicio<fim) {
			int meio = (inicio+fim)/2;
			mergeSort(inicio,meio);
			mergeSort(meio+1,fim);
			merge(inicio,meio,fim);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void merge(int inicio, int meio, int fim) {
		T[] infoMerge = getInfo();
		
		int tamEsquerda = meio - inicio + 1;
		T[] esquerda = (T[]) new Comparable[tamEsquerda];
		for(int i=0 ; i <= tamEsquerda - 1 ; i++) {
			esquerda[i] = infoMerge[inicio+i];
		}
		
		int tamDireita = fim - meio;
		T[] direita = (T[]) new Comparable[tamDireita];
		for(int i=0 ; i <= tamDireita - 1 ; i++) {
			direita[i] = infoMerge[meio+1+i];
		}
		
		int cEsq = 0;
		int cDir = 0;
		
		int i;
		
		for(i = inicio ; i<=fim ; i++) {
			if ((cEsq < tamEsquerda) && (cDir < tamDireita)) {
				if(esquerda[cEsq].compareTo(direita[cDir]) < 0) {
					infoMerge[i] = esquerda[cEsq];
					cEsq++;
				} else {
					infoMerge[i] = direita[cDir];
					cDir++;
				}
			} else {
				break;
			}
		}
		
		while (cEsq < tamEsquerda) {
			infoMerge[i] = esquerda[cEsq];
			cEsq++;
			i++;
		}
		
		while (cDir < tamDireita) {
			infoMerge[i] = direita[cDir];
			cDir++;
			i++;
		}
			
	}
	
	

}
