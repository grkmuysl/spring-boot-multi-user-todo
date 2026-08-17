# Multi User Todo App

Spring Boot öğrenme yolculuğumdaki **3. projem**. [1. projem](https://github.com/grkmuysl/spring-boot-student-crud-api) (Student App) ve [2. projem](https://github.com/grkmuysl/library-management) (Library Management) olan önceki projelerde bilinçli olarak boş bıraktığım konulardan en büyüğünü — **güvenlik** — bu projede ele aldım. Spring Security + JWT ile kimlik doğrulama/yetkilendirme yapan, her kullanıcının yalnızca kendi görevlerini (task) görüp yönetebildiği bir çoklu kullanıcı todo API'si sunuyor.

Bu proje de production için değil, **öğrenme amaçlı** yazılmıştır. Aşağıda "önceki projelere göre neler değişti" ve "hâlâ eksik olanlar" ayrı ayrı listelendi; amaç bu projeyi de sonsuza kadar yamalamak değil, kalan eksikleri bir sonraki projede ele almak.

## Önceki Projelere Göre Neler Değişti / Neler Öğrenildi

- **Kimlik doğrulama (authentication):** `/register` ve `/auth` endpoint'leriyle kullanıcı kaydı ve girişi yapılıyor; giriş başarılı olduğunda JWT (`io.jsonwebtoken` / jjwt) döndürülüyor.
- **Yetkilendirme (authorization):** `SecurityConfig` içinde `/register` ve `/auth` dışındaki tüm endpoint'ler `authenticated()` ile korunuyor; istekler `JwtAuthenticationFilter` üzerinden geçip `SecurityContext`'e yerleştiriliyor.
- **Stateless oturum:** `SessionCreationPolicy.STATELESS` ile klasik session yerine her istekte JWT doğrulaması yapılıyor.
- **Kullanıcı bazlı veri izolasyonu:** `Task` entity'si `User`'a `@ManyToOne` ile bağlı; task'lara erişim `@AuthenticationPrincipal` ile alınan kullanıcı id'sine göre (`findByIdAndUserId`) filtreleniyor, böylece bir kullanıcı başka bir kullanıcının task'ına erişemiyor.
- **Sayfalama (pagination):** Görev listeleme endpoint'i artık `Pageable` / `Page<TaskResponse>` kullanıyor (1. ve 2. projede eksik bırakılan bir konuydu).
- **Daha kapsamlı global hata yönetimi:** `GlobalExceptionHandler`, validasyon dışında `AccessDeniedException` (403), `AuthenticationException` (401), `RuntimeException` (404) ve genel `Exception` (500) için de anlamlı `ErrorResponse` gövdeleri dönüyor; kimlik doğrulama hataları için ayrıca `CustomAuthenticationEntryPoint` eklendi.
- **Rol altyapısı:** `RoleName` (`USER`, `ADMIN`) enum'ı ve `UserDetailsImpl` üzerinden `GrantedAuthority` desteği eklendi (henüz endpoint bazlı yetkilendirmede kullanılmıyor, aşağıya bakınız).
- **Test:** `TaskServiceImpl` için Mockito ile ilk birim testleri yazıldı (1. ve 2. projede test yoktu).
- **API dokümantasyonu:** springdoc-openapi ile Swagger UI, JWT Bearer auth şeması tanımlanmış şekilde geliyor.

## Kullanılan Teknolojiler

- Java 17
- Spring Boot 4.1.0 (Spring Web / MVC, Spring Data JPA, Spring Security)
- PostgreSQL
- JJWT (`jjwt-api` / `jjwt-impl` / `jjwt-jackson`) — JWT üretimi ve doğrulaması
- Bean Validation (`spring-boot-starter-validation`)
- springdoc-openapi (Swagger UI)
- Lombok
- JUnit 5 + Mockito
- Maven

## Mimari

Katmanlı (layered) mimari, güvenlik ve JWT katmanlarıyla genişletilmiş halde:

```
İstek → JwtAuthenticationFilter → Controller (interface) → ControllerImpl → Service (interface) → ServiceImpl → Repository (JPA) → PostgreSQL
```

- `IAuthController` / `AuthController` — kayıt ve giriş (`/register`, `/auth`)
- `ITaskController` / `TaskControllerImpl` — task CRUD, `@AuthenticationPrincipal` ile giriş yapan kullanıcıya göre işlem yapılması
- `IUserService` / `UserService` — kullanıcı kaydı, kimlik doğrulama, JWT üretimi
- `ITaskService` / `TaskServiceImpl` — task iş mantığı, sahiplik (ownership) kontrolü
- `JwtService` — token üretimi, imzalama ve doğrulama (HMAC-SHA)
- `JwtAuthenticationFilter` — her istekte `Authorization: Bearer <token>` başlığını okuyup `SecurityContext`'i dolduran filtre
- `SecurityConfig` — filtre zinciri, endpoint bazlı erişim kuralları, stateless session politikası
- `CustomAuthenticationEntryPoint` / `GlobalExceptionHandler` — kimlik doğrulama/yetkilendirme ve genel hataların anlamlı JSON gövdesiyle (`ErrorResponse`) dönülmesi
- `User` / `Task` — JPA entity'leri (`User` 1–N `Task`)
- `TaskRepository` / `UserRepository` — `JpaRepository`

## API Endpoint'leri

### Auth — Base path: `/`

| Metod | Endpoint    | Açıklama                                       | Yetki        |
| ----- | ----------- | ---------------------------------------------- | ------------ |
| POST  | `/register` | Yeni kullanıcı kaydı oluşturur                 | Herkese açık |
| POST  | `/auth`     | Kullanıcı adı/şifre ile giriş yapar, JWT döner | Herkese açık |

### Task — Base path: `/api/task`

Tüm task endpoint'leri JWT gerektirir (`Authorization: Bearer <token>`) ve yalnızca isteği yapan kullanıcının kendi task'ları üzerinde çalışır.

| Metod  | Endpoint  | Açıklama                                              |
| ------ | --------- | ----------------------------------------------------- |
| POST   | `/create` | Giriş yapan kullanıcı için yeni task oluşturur        |
| GET    | `/list`   | Giriş yapan kullanıcının task'larını sayfalı listeler |
| GET    | `/{id}`   | ID'ye göre tek bir task getirir (sahiplik kontrollü)  |
| PUT    | `/{id}`   | Var olan task'ı günceller (sahiplik kontrollü)        |
| DELETE | `/{id}`   | ID'ye göre task siler (sahiplik kontrollü)            |

### Örnek İstekler

```bash
# Kayıt ol
curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "gorkem", "email": "gorkem@example.com", "password": "sifre123"}'

# Giriş yap ve token al
curl -X POST http://localhost:8080/auth \
  -H "Content-Type: application/json" \
  -d '{"username": "gorkem", "password": "sifre123"}'

# Dönen token ile task oluştur
curl -X POST http://localhost:8080/api/task/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"title": "Spring Security öğren", "description": "JWT filter yaz", "status": "TODO", "dueDate": "2026-08-30T00:00:00"}'

# Task'ları sayfalı listele
curl "http://localhost:8080/api/task/list?page=0&size=10" \
  -H "Authorization: Bearer <TOKEN>"

# Task güncelle
curl -X PUT http://localhost:8080/api/task/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"title": "Spring Security öğren", "description": "JWT filter tamamlandı", "status": "DONE", "dueDate": "2026-08-30T00:00:00"}'

# Task sil
curl -X DELETE http://localhost:8080/api/task/1 \
  -H "Authorization: Bearer <TOKEN>"
```

### Model / DTO

```json
// RegisterRequest
{
  "username": "gorkem",
  "email": "gorkem@example.com",
  "password": "sifre123"
}

// TaskRequest / TaskResponse
{
  "id": 1,
  "title": "Spring Security öğren",
  "description": "JWT filter yaz",
  "status": "TODO",
  "dueDate": "2026-08-30T00:00:00"
}
```

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Veritabanı Kurulumu

Proje PostgreSQL bekliyor. `src/main/resources/application.properties` içinde bağlantı ayarları var; `multiuserdb` adlı bir schema'nın var olması gerekiyor (`hibernate.ddl-auto=update` ile tablolar otomatik oluşturulur/güncellenir, schema'nın kendisi elle oluşturulmalı):

```sql
CREATE SCHEMA IF NOT EXISTS multiuserdb;
```

> **Not:** Bağlantı bilgileri (kullanıcı adı/şifre) ve JWT imzalama anahtarı şu an kaynak kod / `application.properties` içinde açık metin olarak duruyor. Bunu kendi ortamınızda çalıştırmadan önce kendi PostgreSQL kimlik bilgilerinizle güncellemeniz gerekir. Aşağıdaki "eksikler" bölümünde bunun neden iyi bir pratik olmadığından bahsediyorum.

## Kurulum ve Çalıştırma

```bash
git clone https://github.com/grkmuysl/multi-user-todo.git
cd multi-user-todo
./mvnw spring-boot:run
```

Uygulama varsayılan olarak `http://localhost:8080` adresinde ayağa kalkar.

## Hâlâ Eksik Olanlar

Önceki projelerdeki bazı eksikler burada giderildi, ama bu proje de tamamlanmış/production-ready değil. Aşağıdaki maddeler bilinçli ya da geliştirirken fark ettiğim, bir sonraki projede ele alınacak konular:

- **`RuntimeException` çok genel yakalanıyor:** `GlobalExceptionHandler` her `RuntimeException`'ı 404 olarak dönüyor; bu, "not found" olmayan durumlarda da yanıltıcı olabilir. → Bir sonraki projede özel exception sınıfları (`NotFoundException`, `ConflictException` vb.) ile daha doğru HTTP durum kodları dönülecek.
- **Refresh token / logout mekanizması yok:** Token süresi dolduğunda kullanıcının yeniden `/auth`'a gitmesi gerekiyor, token iptali (blacklist) da yok. → Bir sonraki projede refresh token akışıyla ele alınacak.
- **Manuel DTO dönüşümü:** Entity↔DTO dönüşümü elle (`BeanUtils.copyProperties` veya alan alan set) yapılıyor, tip güvenliği yok. → Bir sonraki projede MapStruct gibi bir mapper kütüphanesiyle ele alınacak.
- **Test kapsamı sınırlı:** Yalnızca `TaskServiceImpl.createTask` için birim testleri var; `update`/`delete`/`getTaskById`, controller katmanı ve auth akışı için test yok. → Bir sonraki projede daha kapsamlı JUnit + Mockito testleriyle ele alınacak.

Bu proje de sabit kalacak; yukarıdaki maddeler burada değil, ilerleyen projelerde sıfırdan yazılarak öğrenilecek.

## Lisans

Bu proje kişisel öğrenme amaçlı geliştirilmiştir.
