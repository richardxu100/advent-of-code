(ns advent-of-code-2025.day1.day1
  (:require [clojure.string :as str]))

(def sample-input "./src/advent_of_code_2025/day1/sample_input.txt")
(def real-input "./src/advent_of_code_2025/day1/input.txt")

(defn parse-instruction [s]
  {:dir (if (= \L (first s)) :left :right)
   :rotation (parse-long (apply str (rest s)))})

(comment
  (parse-instruction "L12")
  (parse-instruction "R1")
  (first "l12")
  (apply str (rest "l1")))

(defn parse-instructions [input]
  (->> input
       slurp
       str/split-lines
       (map parse-instruction)))

(def sample-instructions (parse-instructions sample-input))
(def real-instructions (parse-instructions real-input))

(defn add-until-in-range [pos]
  (loop [new-pos pos]
    (if (< new-pos 0)
      (recur (+ new-pos 100))
      new-pos)))

(defn subtract-until-in-range [pos]
  (loop [new-pos pos]
    (if (> new-pos 99)
      (recur (- new-pos 100))
      new-pos)))

(comment
  (add-until-in-range -131)
  (subtract-until-in-range 332))

(defn apply-instruction [pos {dir :dir rotation :rotation}]
  (let [new-pos (if (= dir :left) (- pos rotation) (+ pos rotation))]
    (cond
      (< new-pos 0)
      (add-until-in-range new-pos)
      (> new-pos 99)
      (subtract-until-in-range new-pos)
      :else
      new-pos)))

(comment
  (reduce apply-instruction 50 sample-instructions)
  (apply-instruction 50 (first (parse-instructions sample-input))))

(defn gen-position-history [pos instructions]
  (loop [history [pos]
         remaining-instructions instructions]
    (if (empty? remaining-instructions)
      history
      (recur (conj history (apply-instruction (last history) (first remaining-instructions)))
             (rest remaining-instructions)))))

(comment
  (gen-position-history 50 sample-instructions)
  (gen-position-history 50 real-instructions))

(defn find-password [instructions]
  (let [history (gen-position-history 50 instructions)]
    (count (filter zero? history))))

(comment
  (find-password sample-instructions)
  (find-password real-instructions))

(defn calc-left-crossings [pos rotation]
  (let [new-pos (- pos rotation)]
    (cond
      (> new-pos 0)
      0
      (zero? new-pos)
      1
      (zero? pos)
      (abs (quot new-pos 100))
      :else
      (inc (abs (quot new-pos 100))))))

(comment
  (calc-left-crossings 1 23))

(defn calc-right-crossings [pos rotation]
  (let [new-pos (+ pos rotation)]
    (cond
      (< new-pos 100)
      0
      (= 100 new-pos)
      1
      :else
      (quot new-pos 100))))

(comment
  (calc-left-crossings 5 34)
  (calc-right-crossings 50 63)
  (calc-right-crossings 3 200))

(defn calc-total-zero-crossings [{instruction :instruction pos :pos}]
  (let [{dir :dir rotation :rotation} instruction]
    (if (= dir :left)
      (calc-left-crossings pos rotation)
      (calc-right-crossings pos rotation))))

(defn gen-enriched-history [instructions]
  (let [history (gen-position-history 50 instructions)]
    (loop [enriched-history []
           remaining-instructions instructions
           remaining-history history]
      (if (empty? remaining-instructions)
        enriched-history
        (recur (conj enriched-history {:instruction (first remaining-instructions) :pos (first remaining-history)})
               (rest remaining-instructions)
               (rest remaining-history))))))

(gen-enriched-history sample-instructions)

(defn part2 [instructions]
  (let [enriched-history (gen-enriched-history instructions)]
    (reduce + (map calc-total-zero-crossings enriched-history))))


(part2 sample-instructions)
(part2 real-instructions)


