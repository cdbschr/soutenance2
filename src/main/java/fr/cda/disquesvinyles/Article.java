package fr.cda.disquesvinyles;

import java.net.URL;

/**
 * The type Article.
 */
public class Article {
	private String title;
	private String category;
	private String date;
	private String price;
	private String description;
	private URL url;

	/**
	 * Instantiates a new Article.
	 *
	 * @param title       the title
	 * @param category    the category
	 * @param date        the date
	 * @param price       the price
	 * @param description the description
	 * @param url         the url
	 */
	public Article(String title, String category, String date, String price, String description, URL url) {
		this.title = title;
		this.category = category;
		this.date = date;
		this.price = price;
		this.description = description;
		this.url = url;
	}

	/**
	 * Gets title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title.
	 *
	 * @param title the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets category.
	 *
	 * @param category the category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets date.
	 *
	 * @param date the date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets price.
	 *
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Sets price.
	 *
	 * @param price the price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets url.
	 *
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Sets url.
	 *
	 * @param url the url
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return  "Titre : " + this.getTitle() + '\n' +
						", Genre  : " + this.getCategory() + '\n' +
						", Date : " + this.getDate() + '\n' +
						", Prix : " + this.getPrice() + '\n' +
						", Description : \n" + this.getDescription() + '\n' +
						", Lien : " + this.getUrl() + '\n' +
						"=============================================================" + '\n';
	}
}
