package estrutura.pilha;

public class PilhaVetor <T> implements Pilha<T> {

	private T[] info;
	
	private int limite;
 	
	private int tamanho;
	
	public int getTamanho() {
		return tamanho;
	}
	
	@SuppressWarnings("unchecked")
	public PilhaVetor(int limite) {
		this.limite = limite;
		info = (T[]) new Object[limite];
		tamanho = 0;
	}

	@Override
	public void push(T valor) throws PilhaCheiaException {
		
		if (tamanho == limite) {
            throw new PilhaCheiaException("Pilha cheia!");
		} else {
			info[tamanho] = (T) valor;
			tamanho++;
		}
		
	}
	
	@Override
	public T pop() throws PilhaVaziaException {
		T valor = peek();
		tamanho--;
		info[tamanho] = null;
		
		return valor;
	}

	@Override
	
	public T peek() throws PilhaVaziaException {
		
		if(estaVazia()) {
            throw new PilhaVaziaException("Pilha vazia!");
		} else {
			return info[tamanho-1];
		}
	}

	@Override
	public boolean estaVazia() {
		return (tamanho == 0);
	}

	@Override
	public void liberar() throws PilhaVaziaException {
		
		while(!estaVazia()) {
			pop();
		}
	}

	public String toString() {
		String toString = "";
		
		for(int i = tamanho-1 ; i >= 0 ; i--) {
			toString += info[i].toString();
			if (i != 0) {
				toString += ", ";
			}
		}
		
		return toString;
	}

	public void display() {
		for (int i = tamanho-1 ; i >= 0 ; i--) {
			System.out.println(i + " - " + info[i]);
		}
	}
	
	public void concatenar(PilhaVetor<T> p) throws PilhaCheiaException {
		
		if (getTamanho() + p.getTamanho() <= limite) {
			
			for(int i = 0 ; i < p.getTamanho() ; i++) {
				push(p.info[i]);
			}
			
		} else {
            throw new PilhaCheiaException("Pilha não tem capacidade suficiente!");
		}
		
	}
	
}
