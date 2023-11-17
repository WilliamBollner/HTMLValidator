package estrutura.ordenacao;

public abstract class OrdenacaoAbstract<T extends Comparable<T>> {

	private T[] info;
	
	public T[] getInfo() {
		return info;
	}

	public void setInfo(T[] info) {
		this.info = info;
	}

	public void trocar(int a, int b) {
		if(a==b) {
			return;
		}
		T backup;
		backup = info[a];
		info[a] = info[b];
		info[b] = backup;
	}
	
	public void display() { //código para fins de testes.
		for(int i = 0 ; i < info.length ; i++) {
			System.out.println("[" + i + "] = " + info[i]);
		}
	}
	
	
}
