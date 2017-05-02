module Main where

main = do
     input <- readFile "B-large-practice.in"
     let ls = drop 1 $ map (\x -> read x :: Integer) $ lines input
     let res = map (\x -> joiner $ mkTidy $ digs x) ls
     let f = zipWith (\x y -> "Case #" ++ (show x) ++ ": " ++ (show y)) [1..(length res)] res
     mapM_ putStrLn f


isTidy :: [Int] -> Bool
isTidy (x:[]) = True
isTidy (x:y:rest) | x > y = False
                  | otherwise = isTidy (y:rest)

mkTidy :: [Int] -> [Int]
mkTidy xs | isTidy xs = xs
          | otherwise = mkTidy (mkTidy' xs)
        where mkTidy' :: [Int] -> [Int]
              mkTidy' (x:[]) = [x]
              mkTidy' (x:y:rest) | (x>y) = (x-1):(replicate ((length rest) + 1) 9)
                                 | otherwise = x:(mkTidy' (y:rest))


digs :: Integer -> [Int]
digs = map (read . (:[])) . show

joiner :: [Int] -> Integer
joiner = read . concatMap show