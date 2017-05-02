import Data.Array

cutRod :: [Int] -> Int
cutRod pl = go plen
     where plen = length pl
           pa = listArray (1, plen) pl
           go 0 = 0
           go 1 = pa!1
           go n = maximum ([pa!n] ++ map (\(n1,n2) -> dp!n1 + dp!n2) (cuts n))
           dp = listArray (1, plen) [go i | i <- [1..plen]]



cuts :: Int -> [(Int, Int)]
cuts n = map (\x -> (x, n - x)) ns
    where d = (div) n 2
          ns = [1..d]

--cutRod [1,5,8,9,10,17,17,20] == 22
--cutRod [3,5,8,9,10,17,17,20] == 24