#특정 게시물 삭제시 공통으로 사용되는 스크립트.
$ ->

@deleteThis = (href, idx) ->
  if (confirm('게시물을 삭제하시겠습니까?'))
    location.href = "#{href}/#{idx}"

@deleteThisFromProject = (href, idx, pj_idx) ->
  if (confirm('게시물을 삭제하시겠습니까?'))
    location.href = "#{href}/#{idx}/#{pj_idx}"
