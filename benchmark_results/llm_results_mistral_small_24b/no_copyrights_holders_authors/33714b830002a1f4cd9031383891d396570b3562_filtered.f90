1
  if (f5 ('4') .ne. "abcd") STOP 1
  if (len (f5 ('4')) .ne. 4) STOP 1
contains
  function f5 (c)
    character(len=1_8) :: c
    character(len=scan('123456789', c)) :: f5
    integer :: i
     