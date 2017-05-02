module Main where

main = do
     l <- getLine
     let nums = map (\x -> read x :: Integer) $ words l
     putStrLn $ show $ superDigit (superDigit(head nums)*(last nums))


superDigit :: Integer ->  Integer
superDigit n | n > 10 = superDigit (sum $ digits n)
             | otherwise = n

digits :: Integer -> [Integer]
digits  = map (\x -> read [x] :: Integer) . show