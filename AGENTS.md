# AGENTS.md - Bridge.io Project Development Log

## Project Overview
**Bridge.io** is a Java-based forum and community platform built with Spring Boot, focused on bridge engineering knowledge sharing and discussion. The platform serves as a hub for bridge engineering professionals, students, and enthusiasts to share information, discuss technical topics, and exchange resources.

---

## Initial Release - June 1, 2019

### Commit: `265c8f7` - "更新了Jar包版本" (Updated JAR versions)
**Author**: 隐王爷  
**Date**: June 1, 2019, 19:08:38 +0800

This commit represents the initial project setup and complete implementation of the Bridge.io forum platform.

---

## Technology Stack

### Backend Framework
- **Spring Boot 2.2.0.BUILD-SNAPSHOT**
- **Java 8** (java.version: 1.8)
- **Maven** for dependency management

### Core Dependencies
- **Spring Boot Starter Web** - Web application framework
- **Spring Boot Starter Thymeleaf** - Template engine for server-side rendering
- **MyBatis Spring Boot Starter 2.0.0** - ORM framework
- **MySQL Connector 5.1.34** - Database connectivity
- **Druid 1.1.9** - Alibaba database connection pool
- **PageHelper 1.2.3** - MyBatis pagination plugin
- **Log4j 1.3.8.RELEASE** - Logging framework
- **Commons FileUpload 1.3.3** - File upload handling
- **MyBatis Generator 1.3.7** - Code generation

### Frontend Technologies
- **Bootstrap 3.3.7** - UI framework
- **jQuery 3.3.1** - JavaScript library
- **wangEditor** - Rich text editor
- **Bootstrap Validator** - Form validation
- **Bootstrap File Input** - File upload component

---

## Database Schema

### Core Tables

#### User Management
- **`user_tb`** - User information, roles, experience, sign-in tracking
  - Supports user levels: Regular User (0), Bar Staff (1), Sub-Admin (2), Admin (3), Super Admin (4)
  - User experience system
  - Continuous sign-in tracking
  - Ban/mute functionality
- **`role_tb`** - Role definitions
- **`permission_tb`** - Permission definitions
- **`role_permission_tb`** - Role-permission mapping
- **`user_concern_tb`** - User follow/concern relationships

#### Content Management
- **`post_tb`** - Forum posts with support for:
  - Post types: Introduction (1), Help Request (0)
  - Century classification for historical bridges
  - Status tracking (Normal, Sealed, Under Review)
  - View counts, praise counts, reply tracking
- **`floor_tb`** - Post replies/comments
- **`comment_tb`** - Sub-comments on replies
- **`keyword_tb`** - Post keywords/tags
- **`post_keyword_tb`** - Post-keyword mapping
- **`post_user_tb`** - User-post interactions (likes, etc.)

#### Resource Management
- **`data_tb`** - File resources with:
  - File type support: Word, Text, PPT, Images, Archives
  - Download tracking
  - Approval workflow
- **`img_tb`** - Image storage and management

#### Communication
- **`secret_msg_tb`** - Private messaging between users

#### System
- **`log_tb`** - Comprehensive activity logging
- **`statu_tb`** - Status definitions

---

## Key Features Implemented

### 1. User System
- **Registration & Authentication**
  - User registration with email and phone validation
  - Secure password management
  - Role-based access control (RBAC)

- **User Profiles**
  - Customizable avatars
  - Personal information management
  - Experience point system
  - Sign-in rewards and tracking

- **Social Features**
  - User following/concern system
  - Private messaging
  - User-to-user interactions

### 2. Forum Functionality
- **Four Main Sections**:
  1. **Introduction Section** - Share bridge introductions and discussions
  2. **Resource Section** - Upload/download bridge-related materials
  3. **Help Section** - Q&A and community support
  4. **Hot Posts** - Popular content based on engagement

- **Post Management**
  - Rich text editor integration (wangEditor)
  - Image upload and embedding
  - Keyword/tag system
  - Post status workflow (Draft → Under Review → Published)
  - View tracking and analytics

- **Comment System**
  - Multi-level commenting (posts → floors → comments)
  - Comment likes/praise
  - Reply notifications

### 3. Resource Management
- **File Operations**
  - Multi-format support (DOC, TXT, PPT, images, archives)
  - File upload with validation
  - Download tracking
  - File approval workflow

- **Image Handling**
  - Image upload and storage
  - Integration with posts and user profiles
  - Path-based image management

### 4. Administrative Tools
- **User Management**
  - Add, edit, delete users
  - Role assignment
  - Ban/mute users
  - User activity monitoring

- **Content Moderation**
  - Post review and approval
  - Post sealing/unsealing
  - Post deletion
  - Resource approval

- **System Monitoring**
  - Comprehensive logging system
  - User activity tracking
  - System operation logs

### 5. Search & Discovery
- **Keyword System**
  - Pre-defined bridge categories:
    - Railway bridges (铁路桥)
    - Highway bridges (公路桥)
    - Pedestrian bridges (人行桥)
    - Viaducts (高架桥)
    - Wooden bridges (木桥)
    - Steel bridges (钢桥)
    - Reinforced concrete bridges (钢筋混凝土桥)

- **Classification**
  - Historical period classification (18th century and earlier, 18-19th century, 20th century, 21st century)
  - Post type filtering

### 6. Sample Content
The system includes rich sample data featuring famous Chinese bridges:
- **Zhaozhou Bridge (赵州桥)** - Historical stone arch bridge
- **Guangji Bridge (广济桥)** - Unique "梭船廿四洲" style
- **Lugou Bridge (卢沟桥)** - Historical significance bridge
- Technical discussions and Q&A posts
- Bridge engineering resources and documentation

---

## Project Structure

### Java Packages
```
com.dlu.ghh
├── bean/           - Entity classes (POJOs)
├── controller/     - Web controllers (MVC)
├── mapper/         - MyBatis mappers (DAO)
├── service/        - Business logic services
├── utils/          - Utility classes
├── filter/         - Filters (logging, etc.)
└── test/           - Test classes
```

### Resources
```
src/main/resources/
├── application.properties    - Application configuration
├── bridge.sql               - Database schema and sample data
├── log4j.properties        - Logging configuration
├── mybatis/                - MyBatis XML mappers
├── static/                 - Static assets (CSS, JS, images)
└── templates/              - Thymeleaf templates
```

### Key Controllers
- **UserController** - User management and authentication
- **PostingController** - Post creation and management
- **CommentController** - Comment handling
- **FloorController** - Reply management
- **DataController** - Resource/file management
- **ImageController** - Image operations
- **PostUserController** - User-post interactions
- **UserConcernController** - User following system
- **SecretController** - Private messaging
- **IndexController** - Main navigation and home

---

## Configuration

### Database Configuration
- Database name: `bridge`
- Character set: UTF-8
- MySQL version: 5.0.37-community-nt (compatible)

### Build Configuration
- Maven wrapper included for consistent builds
- MyBatis Generator configuration (MBG.xml)
- Spring Boot Maven Plugin for packaging

---

## Notable Implementation Details

### Security Features
- Password encryption and validation
- Session management (SessionUtils)
- Role-based access control
- User ban/mute functionality

### Performance Optimizations
- Druid connection pooling
- PageHelper for efficient pagination
- Lazy loading where appropriate

### Logging & Monitoring
- MDC (Mapped Diagnostic Context) filter for request tracking
- Comprehensive activity logging
- User action audit trail

### Code Quality
- Clear separation of concerns (MVC pattern)
- Utility classes for common operations
- MyBatis for clean SQL management
- Responsive web design with Bootstrap

---

## Sample Data Highlights

The project includes comprehensive test data:
- **54 users** with various roles
- **Multiple posts** covering bridge engineering topics
- **Hierarchical comments** demonstrating the discussion system
- **File resources** for download
- **Activity logs** showing system usage patterns

---

## Future Development Considerations

Based on the initial implementation, potential areas for enhancement could include:
- Enhanced search functionality
- Real-time notifications
- Mobile application support
- Advanced analytics dashboard
- Multi-language support
- Integration with external bridge databases
- Video content support
- Enhanced security features (2FA, OAuth)

---

## Development Team
- **Initial Developer**: 隐王爷 (Hidden Prince)
- **Project Start Date**: June 1, 2019

---

## License & Attribution
This project was developed as a bridge engineering community platform focused on knowledge sharing and technical discussion in the Chinese bridge engineering community.

---

*Last Updated: June 1, 2019*  
*Document Version: 1.0*
