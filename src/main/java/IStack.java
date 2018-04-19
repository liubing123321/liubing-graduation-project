public interface IStack<E> {

    /**1.判断空栈
     *
     * @return
     */

    public boolean isEmpty();

    /**2.判断栈满
     *
     * @return
     */

    public boolean isMax();

    /**3.入栈
     *
     * @param e
     * @return
     */

    public boolean push(E e);

    /**4.出栈
     *
     * @return
     */

    public E pop();

    /**5.返回栈顶
     *
     * @return
     */

    public E peek();

    /**6.返回元素在栈中的位置
     *
     * @param e
     * @return
     */

    public int getIndex(E e);

    /**7.返回栈的实际长度
     *
     * @return
     */

    public int size();

    /**8.返回栈容量
     *
     * @return
     */

    public int getStackSize();

    /**9.打印栈
     *
     */

    public void display();


}
