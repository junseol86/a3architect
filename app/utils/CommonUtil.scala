package utils

/**
  * Created by Hyeonmin on 2016-11-24.
  */
class CommonUtil {
//  전화번호를 대쉬(-)사인을 기준으로 세 String으로 나누어 반환
  def phoneNoSlice(phoneNo: String) = {
    def sliceProcess(given: String, first: String, second: String, third: String, state: Int): List[String] = {
      if (state == 0) {
        if (given.head != '-')
          sliceProcess(given.tail, first + given.head.toString, second, third, 0)
        else
          sliceProcess(given.tail, first, second, third, 1)
      }
      else if (state == 1) {
        if (given.head != '-')
          sliceProcess(given.tail, first, second + given.head.toString, third, 1)
        else
          sliceProcess(given.tail, first, second, third, 2)
      }
      else {
        if (given.length > 0)
          sliceProcess(given.tail, first, second, third + given.head.toString, 2)
        else
          List(first, second, third)
      }
    }
    sliceProcess(phoneNo.trim, "", "", "", 0)
  }
}
