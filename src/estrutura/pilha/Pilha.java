package estrutura.pilha;

public interface Pilha<T> {

	void push(T v) throws PilhaCheiaException;
	
	T pop() throws PilhaVaziaException;
	
	T peek() throws PilhaVaziaException;
	
	boolean estaVazia();
	
	void liberar() throws PilhaVaziaException;

	
}
