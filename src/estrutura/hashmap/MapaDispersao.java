package estrutura.hashmap;

import estrutura.listaEncadeada.ListaEncadeada;
import estrutura.listaEncadeada.NoLista;

public class MapaDispersao<T> {
	
    private ListaEncadeada<NoMapa<T>>[] info;

	@SuppressWarnings("unchecked")
	public MapaDispersao(int tamanho) {
        info = new ListaEncadeada[tamanho];
    }

    public ListaEncadeada<NoMapa<T>>[] getInfo() {
        return info;
    }

    private int calcularHash(int chave) {
        return chave % info.length;
    }

    public void inserir(int chave, T dado) {
        int idx = calcularHash(chave);
        
        if(info[idx] == null) {
        	info[idx] = new ListaEncadeada<>();
        }
        
        NoMapa<T> no = new NoMapa<>();
        no.setChave(chave);
        no.setInfo(dado);
        
        info[idx].inserir(no);
    }

    public void remover(int chave) {
        int idx = calcularHash(chave);

        if (info[idx] != null) {
        	NoMapa<T> no = new NoMapa<T>();
        	no.setChave(chave);
        	info[idx].retirar(no);
        }
    }

    public T buscar(int chave) {
        int idx = calcularHash(chave);
        
        if (info[idx] != null) {
        	NoMapa<T> noMapa = new NoMapa<T>();
        	noMapa.setChave(chave);
        	
        	NoLista<NoMapa<T>> no;
        	no = info[idx].buscar(noMapa);
        	if (no != null) {
        		return no.getInfo().getInfo();
        	}
        }
        
        return null;
    }
}
