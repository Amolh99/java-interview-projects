# Module 22 - AWS

Receipt storage for the order service. The service depends on a `ReceiptStorage` port. The AWS adapter (S3 with a presigned URL) is shown as a design, and an in-memory adapter runs the tests so no AWS account is needed.

## AWS services mapped to this domain
| Need | AWS service | Notes |
|---|---|---|
| Store PDF receipts | S3 | Versioning, lifecycle rules to Glacier after 90 days |
| Give customers a download link | S3 presigned URL | Expires; avoids proxying bytes through the API |
| Queue shipping jobs | SQS | Standard for throughput, FIFO for ordering |
| Fan out order events | SNS | Pub/sub to several SQS queues |
| Order lookup by id | DynamoDB | Partition key `orderId`, single-digit ms reads |
| Run the API | ECS Fargate or EKS | No servers to patch |
| Secrets | Secrets Manager | Injected at runtime, not in the image |

## Interview questions
1. **S3 consistency?** Strong read-after-write for all operations, since 2020.
2. **Presigned URL vs proxying?** A presigned URL lets the client download directly from S3, saving bandwidth and API threads.
3. **SQS standard vs FIFO?** Standard is at-least-once and unordered. FIFO is exactly-once processing with ordering per message group.
4. **IAM role vs access keys for a container?** Use a task or pod role. Long-lived keys leak more easily.
5. **DynamoDB partition key design?** Choose a key with high cardinality to avoid hot partitions.

## Run
```bash
mvn test
```
