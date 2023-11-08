package estrutura.hashmap;

public class NoMapa<T> {
    private int chave;
    private T info;
    
	public int getChave() {
        return chave;
    }

    public void setChave(int chave) {
        this.chave = chave;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    @SuppressWarnings("unchecked")
	public boolean equals(Object obj) {    
    	if(this == obj) return true; 
    	if(obj == null || getClass() != obj.getClass()) return false;
    	
		NoMapa<T> noMapa = (NoMapa<T>) obj;
		
        return this.getChave() == noMapa.chave;
    }

}
