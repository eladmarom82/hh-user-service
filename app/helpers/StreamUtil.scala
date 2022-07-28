package helpers

import java.util
import java.util.stream.StreamSupport
import java.util.{Spliterator, Spliterators, stream}
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

object StreamUtil {

  def batch(it: util.Iterator[String], batchSize: Int): stream.Stream[ListBuffer[String]] = {
    val listIterator = new Iterator[ListBuffer[String]] {
      override def hasNext: Boolean = it.hasNext

      override def next(): ListBuffer[String] = {
        val list = new ListBuffer[String]
        var i = 0
        while (it.hasNext && i < batchSize) {
          list += it.next
          i += 1
        }
        list
      }
    }

    StreamSupport.stream(Spliterators.spliteratorUnknownSize(listIterator.asJava, Spliterator.ORDERED), false)
  }
}
