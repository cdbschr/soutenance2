package fr.cda.disquesvinyles;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Scrapper {
	public static String scrapDiscogs(String searchTitle, String searchCategory, String searchDate, String searchPriceMin, String searchPriceMax) throws IOException {
		String url = "https://www.discogs.com/fr/search/?q=" + searchTitle + "&type=master&genre_exact=" + searchCategory;
		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlAnchor> article = htmlPage.getByXPath("//div[1]/a");
		for (HtmlElement a : article) {
			String title = a.getTextContent();
			HtmlPage pageArticle = a.click();
			URL pageArticleUrl = pageArticle.getUrl();

			List<HtmlElement> date = pageArticle.getByXPath("//table/tbody/tr[3]/td/a/time");
			List<HtmlElement> price = pageArticle.getByXPath("//div[2]/div[1]/div[11]/section/header/div/span/span");
			List<HtmlElement> desc = pageArticle.getByXPath("//div[2]/div[1]/div[5]/section/div/div");

			for (HtmlElement d : date) {
				String year = d.getTextContent();

				if (Integer.parseInt(year) >= Integer.parseInt(searchDate)) {
					for (HtmlElement p : price) {
						String priceArticle = p.getTextContent()
										.replace("€", "")
										.replace(",", ".")
										.substring(0, 4);

						if ((Double.parseDouble(priceArticle) <= Double.parseDouble(searchPriceMax)) && (Double.parseDouble(priceArticle) >= Double.parseDouble(searchPriceMin))) {
							for (HtmlElement descArt : desc) {
								String descArticle = descArt.getTextContent();
								res += "Titre : " + title + '\n' +
												"Prix : " + priceArticle + " €" + '\n' +
												"Description : \n" + descArticle + '\n' +
												"Lien : " + pageArticleUrl + '\n' +
												"=============================================================" + '\n';
							}
						}

					}
				}
			}
		}
		if (res.equals("")) {
			res = "Pas de résultats trouvés sur Discogs, veuillez réessayer afin d'avoir des résultats.";
		}
		return res;
	}

	public static String scrapFnac(String searchTitle, String searchDate, String searchPriceMin, String searchPriceMax) throws IOException {
		String url = "https://www.fnac.com/SearchResult/ResultList.aspx?SCat=3!1&SDM=list&Search=" + searchTitle;

		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlAnchor> article = htmlPage.getByXPath("//article/form/div[2]/div/p[1]/span/a");

		for (HtmlElement a : article) {
			String title = a.getTextContent();
			HtmlPage pageArticle = a.click();
			URL pageArticleUrl = pageArticle.getUrl();

			List<HtmlElement> date = pageArticle.getByXPath("//div[2]/dl[2]/dd/p");
			List<HtmlElement> price = pageArticle.getByXPath("//span[contains(@class,'userPrice')]");
			List<HtmlElement> desc = pageArticle.getByXPath("//div[2]/div/div[1]/div[2]/div[2]/div[1]/div[2]/div");

			String year = date.get(0).getTextContent()
							.replaceAll("[^0-9]", "");

			if (Integer.parseInt(year) >= Integer.parseInt(searchDate)) {
				for (HtmlElement p : price) {
					String priceArticle = p.getTextContent()
									.replace("€", "")
									.replace(",", ".");

					if ((Double.parseDouble(priceArticle) <= Double.parseDouble(searchPriceMax)) && (Double.parseDouble(priceArticle) >= Double.parseDouble(searchPriceMin))) {
						for (HtmlElement descArt : desc) {
							String descArticle = descArt.getTextContent();
							res += "Titre : " + title + '\n' +
											"Prix : " + priceArticle + " €" + '\n' +
											"Description : \n" + descArticle + '\n' +
											"Lien : " + pageArticleUrl + '\n' +
											"=============================================================" + '\n';
						}
					}
				}
			}
			if (res.equals("")) {
				res = "Pas de résultats trouvés sur Fnac, veuillez réessayer afin d'avoir des résultats.";
			}
		}
		return res;
	}

	public static String scrapVinylCorner(String searchTitle, String searchCategory, String searchDate, String searchPriceMin, String searchPriceMax) throws IOException {
		String url = "https://www.vinylcorner.fr/recherche?controller=search&s=" + searchTitle + "+" + searchCategory;
		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlAnchor> article = htmlPage.getByXPath("//div/div[2]/div[1]/h3/a");

		for (HtmlElement a : article) {
			String title = a.getTextContent();
			HtmlPage pageArticle = a.click();
			URL pageArticleUrl = pageArticle.getUrl();

			List<HtmlElement> date = pageArticle.getByXPath("//div[2]/dic/div/div/section/div[1]/div[2]/div[1]/p[2]/strong");
			List<HtmlElement> price = pageArticle.getByXPath("//div[1]/div[2]/div[2]/div[1]/div/span");
			List<HtmlElement> desc = pageArticle.getByXPath("//div[contains(@class,'product-description']");

			for (HtmlElement d : date) {
				String year = d.getTextContent().substring(d.getTextContent().length() - 4);
				if (Integer.parseInt(year) >= Integer.parseInt(searchDate)) {
					for (HtmlElement p : price) {
						String priceArticle = p.getTextContent();

						if ((Double.parseDouble(priceArticle) <= Double.parseDouble(searchPriceMax)) && (Double.parseDouble(priceArticle) >= Double.parseDouble(searchPriceMin))) {
							String descArticle = desc.get(0).getTextContent();
							res += "Titre : " + title + '\n' +
											"Prix : " + priceArticle + " €" + '\n' +
											"Description : \n" + descArticle + '\n' +
											"Lien : " + pageArticleUrl + '\n' +
											"=============================================================" + '\n';
						}
					}
				}
			}
		}
		if (res.equals("")) {
			res = "Pas de résultats trouvés sur VinylCorner, veuillez réessayer afin d'avoir des résultats.";
		}
		return res;
	}

	public static String scrapLeboncoin(String searchTitle, String searchPriceMin, String searchPriceMax) throws IOException {
		if (searchTitle.contains(" ")) {
			searchTitle = searchTitle.replace(" ", "%20");
		}

		String url = "https://leboncoin.fr/recherche?category=26&text=" + searchTitle + "&price=" + searchPriceMin + "-" + searchPriceMax;
		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlElement> article = htmlPage.getByXPath("//a/div[2]/div[2]/div[1]/p");
		for (HtmlElement a : article) {
			String title = a.getTextContent();
			HtmlPage pageArticle = webClient.getPage(a.click().getUrl());
			URL pageArticleURL = pageArticle.getUrl();

			List<HtmlElement> price = pageArticle.getByXPath("//div[3]/div/span//div/div[1]/div/span");
			List<HtmlElement> desc = pageArticle.getByXPath("//div[5]/div/div/p");

			for (HtmlElement p : price) {
				String priceArticle = p.getTextContent();
				for (HtmlElement d : desc) {
					String descArticle = d.getTextContent();

					res += "Titre : " + title + '\n' +
									"Prix : " + priceArticle + '\n' +
									"Description : \n" + descArticle + '\n' +
									"Lien : " + pageArticleURL + '\n' +
									"=============================================================" + '\n';
				}
			}

		}
		if (res.equals("")) {
			res = "Pas de résultats trouvés sur Leboncoin, veuillez réessayer afin d'avoir des résultats.";
		}
		return res;
	}

	public static String scrapMesVinyles(String searchTitle, String searchDate, String searchPriceMin, String searchPriceMax) throws IOException {
		String url = "https://mesvinyles.fr/fr/recherche?controller=search&s=" + searchTitle;
		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlAnchor> article = htmlPage.getByXPath("//div/div[2]/h3/a");

		for (HtmlElement a : article) {
			String title = a.getTextContent();
			HtmlPage pageArticle = a.click();
			URL pageArticleUrl = pageArticle.getUrl();

			List<HtmlElement> date = pageArticle.getByXPath("//div[1]/div[2]/div[2]/div[1]/p[1]");
			List<HtmlElement> price = pageArticle.getByXPath("//div[1]/div[2]/div[2]/div[2]/form/div[2]/div[1]/div/span");
			for (HtmlElement d : date) {
				String year = d.getTextContent()
								.replaceAll("[^0-9]", "");

				if (Integer.parseInt(year) >= Integer.parseInt(searchDate)) {
					for (HtmlElement p : price) {
						String priceArticle = p.getTextContent();

						if ((Double.parseDouble(priceArticle) <= Double.parseDouble(searchPriceMax)) && (Double.parseDouble(priceArticle) >= Double.parseDouble(searchPriceMin))) {
							res += "Titre : " + title + '\n' +
											"Prix : " + priceArticle + " €" + '\n' +
											"Lien : " + pageArticleUrl + '\n' +
											"=============================================================" + '\n';
						}
					}
				}
			}
		}

		if (res.equals("")) {
			res = "Pas de résultats trouvés sur MesVinyles, veuillez réessayer afin d'avoir des résultats.";
		}
		return res;
	}

	public static String scrapCultureFactory(String searchTitle, String searchCategory, String searchPriceMin, String searchPriceMax) throws IOException {
		String url = "https://culturefactory.fr/recherche?controller=search&s=" + searchTitle + " " + searchCategory;
		String res = "";

		WebClient webClient = new WebClient();

		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage(url);

		List<HtmlElement> article = htmlPage.getByXPath("//article/div[2]/h4/a");

		for (HtmlElement a : article) {
			if (a.getTextContent().contains(searchTitle)) {
				if (a.getTextContent().toLowerCase().contains("cd") || a.getTextContent().toLowerCase().contains("vinyl")) {
					HtmlPage pageArticle = a.click();
					URL pageArticleUrl = pageArticle.getUrl();
					List<HtmlElement> price = pageArticle.getByXPath("//span[@class='price']");
					for(HtmlElement p : price) {
						String priceArticle = p.getTextContent();
						priceArticle = priceArticle.replace("€", "")
										.replace("\u00a0", "")
										.replace(",", ".")
										.replaceAll("\\s+", "");
						if (Double.parseDouble(searchPriceMin) <= Double.parseDouble(priceArticle) && Double.parseDouble(priceArticle) <= Double.parseDouble(searchPriceMax)) {
							String value = ((HtmlHeading1) pageArticle.getByXPath("//h1[@class='h1 name_details']").get(0)).getTextContent();
							List<HtmlElement> description = pageArticle.getByXPath("//div[@class='product-description']");

							for (HtmlElement d : description) {
								res += "Titre : " + value + '\n' +
												"Prix : " + priceArticle + " €" + '\n' +
												"Description : " + d.getTextContent() + '\n' +
												"Lien : " + pageArticle.getUrl() + '\n' +
												"=============================================================" + '\n';
							}
						}
					}
				}
			}
		}
		if (res.equals("")) {
			res = "Pas de résultats trouvés sur CultureFactory, veuillez réessayer afin d'avoir des résultats.";
		}
		return res;
	}
}