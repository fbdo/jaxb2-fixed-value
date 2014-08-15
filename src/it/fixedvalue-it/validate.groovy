try {

    def file = new File(basedir, 'target/generated-sources/xjc/generated/Book.java')
    assert file.exists()

    def lines = file.readLines()
    assert 'protected UnitOfMeasurement unit = UnitOfMeasurement.UNIT;' == lines[48].trim()
    assert 'protected ProductGroup group = ProductGroup.BOOKS;' == lines[50].trim()

    def file2 = new File(basedir, 'target/generated-sources/xjc/generated/Banana.java')
    assert file2.exists()

    def file3 = new File(basedir, 'target/generated-sources/xjc/generated/EthernetCable.java')
    assert file3.exists()

    def file4 = new File(basedir, 'target/generated-sources/xjc/generated/Product.java')
    assert file4.exists()


    return true

} catch(Throwable e) {
    e.printStackTrace()
    return false
}