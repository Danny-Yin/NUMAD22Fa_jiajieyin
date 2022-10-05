package edu.northeastern.NUMAD22Fa_jiajieyin;


public class LinkItem implements LinkItemClickListener{

    private final String linkName;
    private final String linkURL;

    public LinkItem(String linkName,String linkURL){
        this.linkName = linkName;
        this.linkURL = linkURL;
    }

    public String getLinkName(){
        return linkName;
    }

    public  String getLinkURL(){
        return linkURL;
    }

    public void onLinkItemClick(int position){

    }


}
