# Tugas Besar 1 IF2211 Strategi Algoritma

Kelompok:

1. Gerald Abraham Sianturi (NIM : 13520138)
2. Azka Syauqy Irsyad (NIM : 13520107)
3. Vincent Prasetiya Atmadja (NIM : 13520099)

Repository ini dibuat dalam rangka memenuhi Tugas Besar 1 IF2211 Strategi Algoritma 2021/2022. Di dalam repository ini diterapkan starter bot untuk Entellect Challenge Overdrive dengan memanfaatkan algoritma Greedy

## Penjelasan Singkat Algoritma Greedy

Strategi Greedy yang diimplementasikan oleh kami adalah dengan memasukkan command (dibagi menjadi daftar command di depan dan belakang) ke dalam suatu array, yang kemudian akan dilakukan pembobotan terhadap setiap commandnya. Pembobotan yang dilakukan adalah pergeseran maju mobil, perubahan speed mobil, power up yang didapat, dan bonus pemakaian command atau power up berdasarkan kondisi tertentu.

Dalam tiap roundnya, prioritas penggunaan command adalah langkah ketika sudah mencapai block 1485, fix command ketika damage >= 2, dan command dengan bobot maksimal dari array command yang dilakukan tadi, tentu dengan penyesuaian-penyesuaian tertentu.


## Requirement
1. Java JDK 1.8
2. Apache Maven
## How to Run
1. Build menggunakan IntellIJ
2. Hasil .jar dari build dimasukkan ke dalam folder starter-bots di dalam starter-pack
3. Masukkan ke dalam folder bin atau target
4. Edit file bot.json untuk nickname dan targetnya
5. Jika sudah, jalankan run.bat dengan klik dua kali