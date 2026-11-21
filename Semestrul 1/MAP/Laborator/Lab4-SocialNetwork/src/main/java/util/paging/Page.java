package util.paging;

/**
 * Represents a page of elements along with the total number of available elements.
 *
 * @param <E> the type of elements on the page
 */
public class Page<E> {
    private Iterable<E> elementsOnPage;
    private int totalNumberOfElements;

    /**
     * Constructs a new Page object.
     *
     * @param elementsOnPage the list of elements on the current page
     * @param totalNumberOfElements the total number of elements across all pages
     */
    public Page(Iterable<E> elementsOnPage, int totalNumberOfElements){
        this.elementsOnPage = elementsOnPage;
        this.totalNumberOfElements = totalNumberOfElements;
    }

    /**
     * Returns the elements on the current page.
     *
     * @return the list of elements on this page
     */
    public Iterable<E> getElementsOnPage(){
        return elementsOnPage;
    }

    /**
     * Returns the total number of elements across all pages.
     *
     * @return the total number of elements
     */
    public int getTotalNumberOfElements(){
        return totalNumberOfElements;
    }
}
