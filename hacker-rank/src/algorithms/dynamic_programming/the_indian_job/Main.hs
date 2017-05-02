module Main where

main = do
      tl <- getLine
      let t = read tl :: Int
      go t
          where go 0 = return ()
                go tc = do
                    l1 <- getLine
                    l2 <- getLine
                    let [n,g] = map (\x -> read x :: Int) $ words l1
                    let a = map (\x -> read x :: Int) $ words l2
                    let possible = (sum a) <= 2*g
                    let res = rob a (g,g)
                    if(possible && res) then (putStrLn "YES") else (putStrLn "NO")  
                    go (tc-1)


rob :: [Int] -> (Int, Int) -> Bool
rob [] (_, _) = True
rob (x:xs) (t1, t2) | (t1-x)>=0 && (t2-x)>=0 && t1/=t2 = (rob xs (t1-x,t2)) || (rob xs (t1,t2-x)) 
                  | (t1-x)>=0 = rob xs (t1-x, t2)
                  | (t2-x)>=0 = rob xs (t1, t2-x) 
                  | otherwise = False