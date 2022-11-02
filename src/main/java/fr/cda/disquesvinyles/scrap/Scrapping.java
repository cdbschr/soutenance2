package fr.cda.disquesvinyles.scrap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scrapping {
	private String title;
	private String category;
	private int date;
	private int priceMin;
	private int priceMax;

	public Scrapping(String title, String category, int date, int priceMin, int priceMax) {
		this.title = title;
		this.category = category;
		this.date = date;
		this.priceMin = priceMin;
		this.priceMax = priceMax;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(int priceMin) {
		this.priceMin = priceMin;
	}

	public int getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(int priceMax) {
		this.priceMax = priceMax;
	}

	@Override
	public String toString() {
		return "Titre de l'album : " + title + '\n' +
						", categorie : " + category + '\n' +
						", date : " + date +
						", priceMin : " + priceMin + '€' +
						", priceMax : " + priceMax + '€';
	}
}