package main.entities;

public class Album {
    private int id;
    private String nome;
    private int paginas;
    private byte[] capa;


    public Album(int id, String nome, int paginas, byte[] capa) {
        this.id = id;
        this.nome = nome;
        this.paginas = paginas;
        this.capa = capa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }

    public byte[] getCapa() { return capa; }
    public void setCapa(byte[] capa) { this.capa = capa; }

}
