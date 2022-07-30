package puller

case class LinesSummary(total: Long, processed: Long, failed: Long) {
  def +(that: LinesSummary) = LinesSummary(this.total + that.total, this.processed + that.processed, this.failed + that.failed)

  override def toString: String = s"$total total, $processed processed, $failed failed"
}

object LinesSummary {
  def apply(processedLines: Long, failedLines: Long): LinesSummary = LinesSummary(processedLines + failedLines, processedLines, failedLines)
}
