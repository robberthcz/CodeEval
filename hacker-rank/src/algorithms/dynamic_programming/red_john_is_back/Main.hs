module Main where 

import Data.Array

main = do
    l <- getLine
    let t = read l :: Int
    go t
        where go 0 = return ()
              go s = do
                  l <- getLine
                  let n = read l :: Int
                  let a = listArray (1,217286) (take 217286 $ primeCumSum)
                  putStrLn $ show $ a!(configs!n)
                  go (s-1)
                  

configs :: Array Int Int
configs = a
     where go 1 = 1
           go 2 = 1
           go 3 = 1
           go 4 = 2
           go i = a!(i-1) + a!(i-4)
           a = listArray (1,40) [go i | i <- [1..40]]

primeCumSum :: [Int]
primeCumSum = go 1 0 primes
    where go n c (p:ps) = if(n==p) then (c+1):(go (n+1) (c+1) ps) else c:(go (n+1) c (p:ps)) 

primeCumSum2 :: [Int]
primeCumSum2 = map (\x -> length $ takeWhile (\y -> y <= x) primes) [1..]

--http://stackoverflow.com/questions/11768958/prime-sieve-in-haskell
primes :: [Integer]
primes = 2 : filter (isPrime primes) [3,5..]
  where isPrime (p:ps) n = p*p > n || n `rem` p /= 0 && isPrime ps n

 