
takeWhileAdj :: (Ord a) => [a] -> (a -> a -> Bool) -> [a]
takeWhileAdj [] f = []
takeWhileAdj (x:[]) f = [x]
takeWhileAdj (x:y:rest) f | f x y = x:(takeWhileAdj(y:rest) f)
                          | otherwise = [x]

groupWhileAdj :: (Ord a) => [a] -> (a -> a -> Bool) -> [[a]]
groupWhileAdj [] f = [[]]
groupWhileAdj xs f = adj:(groupWhileAdj rest f)
     where adj = takeWhileAdj xs f
           rest = drop (length adj) xs