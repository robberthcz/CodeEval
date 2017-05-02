module Main where


main = do
     l1 <- getLine
     l2 <- getLine
     l3 <- getLine
     let [n,c] = map (\x -> read x :: Int) $ words l1
     let a = map (\x -> min(read x :: Int) c) $ words l2
     let b = map (\x -> read x :: Int) $ words l3
     let d = map (\(x,y,z) -> min (x - y) (c-z)) (zip3 a b ((tail a) ++ [head a]))
     if (sum d < 0) then putStrLn "0" else putStrLn $ show $ iter d c

iter :: [Int] -> Int -> Int
iter xs c = iter' (reverse xs) []
    where iter' [] zs = length zs
          iter' (y:[]) zs | y < 0 = iter' (y:(reverse zs)) []
                          | otherwise = iter' [] (y:zs)
          iter' (y1:y2:ys) zs | y1 < 0 = iter' ((min (y1+y2) c):ys) zs
                              | otherwise = iter' (y2:ys) (y1:zs)