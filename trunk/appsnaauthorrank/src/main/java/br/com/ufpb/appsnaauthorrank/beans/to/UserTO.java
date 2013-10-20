package br.com.ufpb.appsnaauthorrank.beans.to;

public class UserTO {
	
	private String Id;	
	private String Nome;
	private String Screename;
	private String Biografia;
	private String Localização;
	private String TotalFollowers;
	private String TotalFollowing;
	private String TotalTweets;	
	private String URL;
	private String TimeZone;
	private String Linguagem;
	private String DataDeCriacao;
	private String URLImage;
	
	
	
	public UserTO() {
		super();		
	}

	public UserTO(String id, String nome, String screename, String biografia,
			String localização, String totalFollowers, String totalFollowing,
			String totalTweets, String uRL, String timeZone,
			String linguagem, String dataDeCriacao, String uRLImage) {
		super();
		Id=id;
		Nome = nome;
		Screename = screename;
		Biografia = biografia;
		Localização = localização;
		TotalFollowers = totalFollowers;
		TotalFollowing = totalFollowing;
		TotalTweets = totalTweets;		
		URL = uRL;
		TimeZone = timeZone;
		Linguagem = linguagem;
		DataDeCriacao = dataDeCriacao;
		URLImage = uRLImage;
	}

	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getScreename() {
		return Screename;
	}

	public void setScreename(String screename) {
		Screename = screename;
	}

	public String getBiografia() {
		return Biografia;
	}

	public void setBiografia(String biografia) {
		Biografia = biografia;
	}

	public String getLocalização() {
		return Localização;
	}

	public void setLocalização(String localização) {
		this.Localização=localização;
	}

	public String getTotalFollowers() {
		return TotalFollowers;
	}

	public void setTotalFollowers(String totalFollowers) {
		TotalFollowers = totalFollowers;
	}

	public String getTotalFollowing() {
		return TotalFollowing;
	}

	public void setTotalFollowing(String totalFollowing) {
		TotalFollowing = totalFollowing;
	}

	public String getTotalTweets() {
		return TotalTweets;
	}

	public void setTotalTweets(String totalTweets) {
		TotalTweets = totalTweets;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getTimeZone() {
		return TimeZone;
	}

	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}

	public String getLinguagem() {
		return Linguagem;
	}

	public void setLinguagem(String linguagem) {
		Linguagem = linguagem;
	}

	public String getDataDeCriacao() {
		return DataDeCriacao;
	}

	public void setDataDeCriacao(String dataDeCriacao) {
		DataDeCriacao = dataDeCriacao;
	}

	public String getURLImage() {
		return URLImage;
	}

	public void setURLImage(String uRLImage) {
		URLImage = uRLImage;
	}
	
	
	

	
}