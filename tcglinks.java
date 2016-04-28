/**
*Sample TCGPlayer Link:
*
*http://shop.tcgplayer.com/productcatalog/product/show?newSearch=false&ProductType=All&IsProductNameExact=false&ProductName=black%20lotus
*
*Starcity Games
*http://sales.starcitygames.com/search.php?substring=black+lotus&go.x=25&go.y=5&go=GO&t_all=All&start_date=2010-01-29&end_date=2012-04-22&order_1=finish&limit=25&action=Show%2BDecks&card_qty%5B1%5D=1
*
*Channel Fireball:
*http://store.channelfireball.com/products/search?query=black+lotus
*/

public void createTCGPLink(String cardName){
	String[] tokens = cardName.split(" ");
	String result = ""
	for(int i = 0; i<tokens.length(); i++){
		if (result.equals("")){
			result+=tokens[i];
		}
		else
		{
			result+= "%20"+tokens[i];
		}
	}
	return "http://shop.tcgplayer.com/productcatalog/product/show?newSearch=false&ProductType=All&IsProductNameExact=false&ProductName="+result
}

public void createSCLink(String cardName){
	String[] tokens = cardName.split(" ");
	String result = ""
	for(int i = 0; i<tokens.length(); i++){
		if (result.equals("")){
			result+=tokens[i];
		}
		else
		{
			result+= "+"+tokens[i];
		}
	}
	return "http://sales.starcitygames.com/search.php?substring="+result+"&go.x=25&go.y=5&go=GO&t_all=All&start_date=2010-01-29&end_date=2012-04-22&order_1=finish&limit=25&action=Show%2BDecks&card_qty%5B1%5D=1"
}

public void createCFLink(String cardName){
	String[] tokens = cardName.split(" ");
	String result = "";
	for(int i = 0; i<tokens.length(); i++){
		if (result.equals("")){
			result+=tokens[i];
		}
		else
		{
			result+= "+"+tokens[i];
		}
	}
	return "http://store.channelfireball.com/products/search?query="+result
}