package util.paging;

/**
 * Represents a pagination request containing the page number and page size.
 */
public class Pageable {
    private int pageNumber;
    private int pageSize;

    /**
     * Constructs a new Pageable object.
     *
     * @param pageNumber the page index
     * @param pageSize the number of elements per page
     */
    public Pageable(int pageNumber, int pageSize){
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * Returns the page number.
     *
     * @return the page number
     */
    public int getPageNumber(){
        return pageNumber;
    }

    /**
     * Returns the maximum number of elements per page.
     *
     * @return the page size
     */
    public int getPageSize(){
        return pageSize;
    }
}
